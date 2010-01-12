from twisted.internet.protocol import Protocol, Factory
from twisted.internet import reactor

from simulation import Simulation

class Passthru(Protocol):
    def dataReceived(self, data):
        print 'received', data
        self.factory.simulation.message(data)

class BaerlionFactory(Factory):
    protocol = Passthru

    def __init__(self):
        self.simulation = Simulation()

if __name__ == '__main__':
    factory = BaerlionFactory()
    reactor.listenTCP(6123, factory)
    reactor.run()

