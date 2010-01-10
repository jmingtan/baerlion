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
        if file is None and self.__class__.loaded is False:
            file = self.__class__.filename
        if len(file) > 0 and self.__class__.loaded is False:
            self.__class__.data = utils.loadData(file)
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
        for i in range(self.__class__.data['villagers']):
            self.villagers.append(Villager("Villager #%s" % i))

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
        """Runs one step of the simulation"""
        self.steps += 1
        if self.steps == self.__class__.data['day length']:
            self.days += 1
            self.steps = 0
        print "Day %d, Step %d" % (self.days, self.steps)
        for villager in self.villagers:
            villager.update(self.days, self.days)
            print "%s at %s doing %s" % (villager.name, villager.location, villager.action)

class Villager(Data):
    filename = 'villager.json'

    def __init__(self, name):
        #self.__load__()
        self.name = name
        self.action = None
        self.location = None

    def update(self, days, steps):
        """Tells a villager to do something at his current location"""
        pass

class BaerlionGame(Data):
    filename = 'baerlion.json'

    def __init__(self):
        self.__load__()
        self.sim = Simulation()

    def run(self):
        interval = self.__class__.data['interval']
        while True:
            utils.wait(interval)
            self.update()

    def update(self):
        self.sim.step()

if __name__ == '__main__':
    BaerlionGame().run()
