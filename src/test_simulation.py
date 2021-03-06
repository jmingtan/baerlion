from random import random

import simulation

class TestField:
    def setup(self):
        self.field = simulation.Field(None)
        assert self.field.bounty == 0

    def teardown(self):
        self.field = None

    def test_sown_with_weeds(self):
        field = self.field
        days = 10
        field._state = field.__class__.states['sown']
        field._weed_time = 5
        field._weeded_date = 5
        field._sown_date = field._harvest_time = days
        field._sown(days)
        assert field._state == field.__class__.states['weeds']

    def test_sown_and_now_ripe(self):
        field = self.field
        days = 10
        field._state = field.__class__.states['sown']
        field._weed_time = days
        field._weeded_date = days
        field._sown_date = 5
        field._harvest_time = 5
        field._sown(days)
        assert field._state == field.__class__.states['ripe']

    def test_weeds(self):
        field = self.field
        assert field.bounty == 0
        field._weeds(None)
        assert field.bounty == -1

    def test_update(self):
        expected_days = 2
        def transition(result):
            assert result == expected_days
        def state(self, result):
            assert result == expected_days
        field = self.field
        field._today = 1
        field._transition = transition
        field._state = state
        field.update(expected_days, None)

class TestFarmer:
    def setup(self):
        self.farmer = simulation.Farmer(None, None)
        self.old_responses = simulation.Farmer.responses

    def teardown(self):
        self.farmer = None
        simulation.Farmer.responses = self.old_responses

    def test_ripe(self):
        bounty = 10
        farmer = self.farmer
        assert farmer.inventory == 0
        class Field:
            def harvest(self):
                self.harvest_called = True
                return bounty
        field = Field()
        farmer._ripe(field)
        assert field.harvest_called
        assert farmer.inventory == bounty

    def test_fallow(self):
        farmer = self.farmer
        class Field:
            def sow(self):
                self.sow_called = True
        field = Field()
        farmer._fallow(field)
        assert field.sow_called

    def test_sown(self):
        farmer = self.farmer
        class Field:
            def weed(self):
                self.weed_called = True
        field = Field()
        farmer._sown(field)
        assert field.weed_called

    def test_weeds(self):
        farmer = self.farmer
        class Field:
            def weed(self):
                self.weed_called = True
        field = Field()
        farmer._weeds(field)
        assert field.weed_called

    def test_store(self):
        farmer = self.farmer
        farmer.workplace, farmer.field = 1, 2
        class Granary:
            def store(self, i):
                self.i = i
        granary = Granary()
        inventory = 10
        farmer._store(granary, inventory)
        assert granary.i == inventory
        assert farmer.field == farmer.workplace

    def test_update_with_harvest(self):
        class HarvestLocation:
            def __init__(self):
                self._state = 'state'
            def harvest(self):
                pass
        farmer = self.farmer
        expected = random()
        responses = {'state': lambda x, y: expected}
        farmer.__class__.responses = responses
        harvest_loc = HarvestLocation()
        assert farmer.update(harvest_loc) == expected

    def test_update_with_store(self):
        class StoreLocation:
            def store(self):
                pass
        expected_inventory = 10
        responses = {'granary': lambda x, y, z: (y, z)}
        farmer = self.farmer
        farmer.__class__.responses = responses
        farmer.inventory = expected_inventory
        store_loc = StoreLocation()
        assert farmer.update(store_loc) == (store_loc, expected_inventory)

class TestGranary:
    @classmethod
    def setup_class(self):
        self.granary = simulation.Granary()
        self.old_limit = simulation.Granary.data['limit']

    @classmethod
    def teardown_class(self):
        self.granary = None
        simulation.Granary.data['limit'] = self.old_limit

    def test_store(self):
        limit = 10
        granary = self.granary
        granary.__class__.data['limit'] = limit
        assert granary.amount == 0
        granary.store(5)
        assert granary.amount == 5
        granary.store(5)
        assert granary.amount == 10
        granary.store(5)
        assert granary.amount == 10

