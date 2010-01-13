import utils

class Data:
    """Represents a class backed by a json data file

    Loads a JSON object referenced by the 'filename' class attribute
    into the 'data' class attribute. The attributes for this base class
    are empty and must be subclassed to have any use.
    """
    filename = ''
    data = None
    loaded = False

    def __load__(self, file=None):
        """Loads JSON data from file

        >>> testname = 'sample.json'
        >>> Data().__load__(testname)
        >>> Data.data
        ['sample data']
        >>> Data.filename == testname
        True
        >>> Data.loaded
        True
        """
        if self.__class__.data is None:
            if file is None and self.__class__.loaded is False:
                file = self.__class__.filename
            if len(file) > 0 and self.__class__.loaded is False:
                self.__class__.data = utils.load_data(file)
                self.__class__.filename = file
                self.__class__.loaded = True

class Simulation(Data):
    """A simulation which takes in command/parameter strings
    """

    filename = 'simulation.json'

    def __init__(self):
        self.__load__()
        self.steps = 0
        self.days = 0
        self.villagers = []
        self.location_factory = LocationFactory()
        for i in range(self.__class__.data['villagers']):
            self.villagers.append(Villager("Villager #%s" % i, self.location_factory))

    def message(self, msg):
        """Perform an action based on the command string"""
        cmd, params = self.parse(msg)
        if hasattr(self, cmd):
            func = getattr(self, cmd)
            if params is None:
                func()
            else:
                func(*params)

    def parse(self, msg):
        """Parse a command string and returns its elements

        Returns tuple (command, None) if string consists only of the command, otherwise
        returns (command, params) with command as the first element and list of params
        as the second.

        """
        elems = msg.split('//')
        if len(elems) > 1:
            return (elems[0], elems[1:])
        else:
            return (elems[0], None)

    def step(self):
        """Runs one step of the simulation

        For every step, the order of updating is:
            1. Locations
            2. Villagers
        """
        self.steps += 1
        if self.steps == self.__class__.data['day length']:
            self.days += 1
            self.steps = 0
        print "Day %d, Step %d" % (self.days, self.steps)

        self.location_factory.update(self.days, self.steps)
        for villager in self.villagers:
            villager.update(self.days, self.steps)
            print villager

class LocationFactory(object):
    """Takes care of location life cycle

    If any location managed by LocationFactory implements an update method,
    it must follow the signature update(days, steps) where days is the
    number of days and steps is the time within a day.
    """

    def __init__(self):
        self.locations = []

    def update(self, days, steps):
        for locale in self.locations:
            if hasattr(locale, 'update'):
                locale.update(days, steps)

    def get_home(self, name):
        home = Home(name)
        self.locations.append(home)
        return home

    def get_field(self, name):
        field = Field(name)
        self.locations.append(field)
        return field

    def get_granary(self):
        if hasattr(self.__class__, '_granary'):
            return self.__class__._granary
        else:
            granary = Granary()
            self.__class__.granary = granary
            self.locations.append(granary)
            return self.__class__.granary

class Villager(Data):
    """A simple villager"""

    filename = 'villager.json'

    def __init__(self, name, location_factory):
        self.__load__()
        self.name = name
        self.action = None
        self.home = location_factory.get_home("%s's home" % self.name)
        self.location = self.home
        self.occupation = Farmer(
            location_factory.get_field("%s's field" % self.name),
            location_factory.get_granary()
        )

    def __str__(self):
        return "%s is at %s %s." % (self.name, self.location, self.action)

    def update(self, days, steps):
        """Tells a villager to do something at his current location"""
        start_time = self.__class__.data['start time']
        end_time = self.__class__.data['end time']
        if steps >= start_time and steps <= end_time:
            self.location = self.occupation.workplace
            self.action = self.occupation.update(self.location)
        else:
            self.action = 'resting'
            self.location = self.home

class Home(Data):
    """A simple residence"""

    def __init__(self, name):
        self.name = name

    def __str__(self):
        return self.name

class Field(Data):
    """Describes the behaviour of a crop"""
    filename = 'farming.json'

    def _fallow(self, days):
        """Crop is lying fallow"""
        pass

    def _sown(self, days):
        """Crop has been sown with grain.

        An unweeded crop will soon be overgrown with weeds.
        After some time, the crop will be ready for harvest.

        """
        if days - self._weeded_date >= self._weed_time :
            self._state = Field.states['weeds']
            print "%s has been overgrown with weeds." % self.name
        else:
            self.bounty += 1
        if days - self._sown_date >= self._harvest_time:
            self._state = Field.states['ripe']
            print "%s is now ripe for harvest." % self.name

    def _weeds(self, days):
        """Crop has been overgrown with weeds"""
        self.bounty -= 1

    def _ripe(self, days):
        """Crop is ripe for harvest"""
        pass

    states = {
        'fallow': _fallow,
        'sown': _sown,
        'weeds': _weeds,
        'ripe': _ripe,
    }

    def __init__(self, name):
        self.__load__()
        self._weed_time = self.__class__.data['sowing']['days before weeds']
        self._harvest_time = self.__class__.data['sowing']['days before harvest']
        self._state = Field.states['fallow']
        self._sown_date = 0
        self._weeded_date = 0
        self._harvest_date = 0
        self._today = 0
        self._actions_performed_today = 0
        self.bounty = 0
        self.name = name

    def __str__(self):
        return self.name

    def _allow(self, needed):
        """Whether the action has been successful"""
        self._actions_performed_today += 1
        if self._actions_performed_today >= needed:
            self._actions_performed_today = 0
            return True
        return False

    def _transition(self, days):
        """A day has passed in the life of this field

        Weeds grow overnight in the field, erasing any partial effort made.
        """
        self._today = days
        self._actions_performed_today = 0

    def harvest(self):
        """Harvest wheat, returns some amount of wheat

        Returns None if harvesting is still in progress.
        """
        if self._allow(self.__class__.data['harvesting']['actions needed per day']):
            self._harvest_date = self._today
            bounty, self.bounty = self.bounty, 0
            self._state = Field.states['fallow']
            print 'Field is now fallow.'
            return bounty
        return None

    def sow(self):
        """Sow grain"""
        if self._allow(self.__class__.data['sowing']['actions needed per day']):
            self._sown_date = self._weeded_date = self._today
            self._state = Field.states['sown']
            print "%s has now been sown with grain." % self.name

    def weed(self):
        """Pull weeds"""
        if self._allow(self.__class__.data['weeding']['actions needed per day']):
            self._weeded_date = self._today
            self._state = Field.states['sown']
            print "%s has now been cleared of weeds." % self.name

    def update(self, days, steps):
        """Crop changes depending on the time and actions taken on it"""
        if self._today != days:
            self._transition(days)
        self._state(self, days)

class Farmer(Data):
    """Describes the farming occupation"""
    filename = 'farming.json'

    def _fallow(self, field):
        field.sow()
        return 'sowing'

    def _sown(self, field):
        field.weed()
        return 'pulling weeds'

    def _weeds(self, field):
        field.weed()
        return 'pulling weeds'

    def _ripe(self, field):
        bounty = field.harvest()
        if bounty is not None:
            self.inventory = bounty
            self.workplace = self.granary
            return "has harvested %d units of grain" % self.inventory
        return 'harvesting'

    def _store(self, granary, inventory):
        granary.store(inventory)
        self.workplace = self.field
        return 'storing grain at the granary'

    responses = {
        Field.states['fallow']: _fallow,
        Field.states['sown']: _sown,
        Field.states['weeds']: _weeds,
        Field.states['ripe']: _ripe,
        'granary': _store,
    }

    def __init__(self, field, granary):
        self.__load__()
        self.field = field
        self.granary = granary
        self.workplace = field
        self.inventory = 0

    def update(self, location):
        """Figures out what to do at a given location, returns an action"""
        if hasattr(location, 'harvest'):
            field = location
            return Farmer.responses[field._state](self, field)
        if hasattr(location, 'store'):
            granary = location
            inventory, self.inventory = self.inventory, 0
            return Farmer.responses['granary'](self, granary, inventory)

class Granary(Data):
    """Describes a granary for the village"""

    filename = 'granary.json'

    def __init__(self):
        self.__load__()
        self.amount = 0

    def __str__(self):
        return 'the granary'

    def store(self, amount):
        limit = self.__class__.data['limit']
        if self.amount + amount > limit:
            print "The granary has too much grain, unable to store anymore!"
        else:
            self.amount += amount
            print "The granary now has %s units of grain." % self.amount

class BaerlionGame(Data):
    """Allows standalone mode for baerlion"""

    filename = 'baerlion.json'

    def __init__(self):
        self.__load__()
        self.sim = Simulation()

    def run(self):
        interval = self.__class__.data['seconds per step']
        while True:
            utils.wait(interval)
            self.update()

    def update(self):
        self.sim.step()

if __name__ == '__main__':
    BaerlionGame().run()
