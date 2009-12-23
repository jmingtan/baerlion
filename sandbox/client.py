from twisted.internet.protocol import Protocol, ClientCreator
from twisted.internet import reactor

class Greeter(Protocol):
	def sendMessage(self, msg):
		self.transport.write("msg: %s" % msg)

def gotProtocol(p):
	p.sendMessage("Hello")
	reactor.callLater(1, p.sendMessage, "This is sent in a sec")
	reactor.callLater(2, p.transport.loseConnection)

c = ClientCreator(reactor, Greeter)
c.connectTCP("127.0.0.1", 6123).addCallback(gotProtocol)
reactor.run()
