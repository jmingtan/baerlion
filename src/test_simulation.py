import time
from random import random
from nose import with_setup

import simulation

class TestFarmer:
    @classmethod
    def setup_class(self):
        self.farmer = simulation.Farmer(None, None)
        self.old_responses = simulation.Farmer.responses

    @classmethod
    def teardown_class(self):
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
        farmer.inentory = expected_inventory
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

