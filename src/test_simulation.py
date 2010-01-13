import time

from nose import with_setup
import simulation

class TestFarmer():
    @classmethod
    def setup_class(self):
        self.farmer = simulation.Farmer(None, None)

    @classmethod
    def teardown_class(self):
        self.farmer = None

    def test_ripe(self):
        bounty = 10
        farmer = self.farmer
        assert farmer.inventory == 0
        class Field():
            def harvest(self):
                self.harvest_called = True
                return bounty
        field = Field()
        farmer._ripe(field)
        assert field.harvest_called
        assert farmer.inventory == bounty

class TestGranary():
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

