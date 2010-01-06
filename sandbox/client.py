from twisted.internet.protocol import Protocol, ClientCreator
from twisted.internet import reactor

class Runner(Protocol):
	def sendMessage(self, msg):
		self.transport.write(msg)

def gotProtocol(p):
	p.sendMessage("step")
	#reactor.callLater(1, p.sendMessage, "This is sent in a sec")
	reactor.callLater(2, p.transport.loseConnection)

c = ClientCreator(reactor, Runner)
c.connectTCP("127.0.0.1", 6123).addCallback(gotProtocol)
reactor.run()
