from twisted.internet.protocol import Protocol, ClientCreator
from twisted.internet import reactor

UPDATE_PERIOD = 1.0

class Runner(Protocol):
    def update(self, msg):
        print 'sending', msg
        self.transport.write(msg)
        self.updateCall = reactor.callLater(UPDATE_PERIOD, self.update, "step")

    def connectionMade(self):
        self.updateCall = reactor.callLater(UPDATE_PERIOD, self.update, "step")

    def connectionLost(self, reason):
        self.updateCall.cancel()

if __name__ == "__main__":
    c = ClientCreator(reactor, Runner)
    c.connectTCP("127.0.0.1", 6123)
    reactor.run()
