from twisted.internet.protocol import Protocol, Factory
from twisted.internet import reactor

class Echo(Protocol):
	def dataReceived(self, data):
		print data
		#self.transport.write(data)

factory = Factory()
factory.protocol = Echo

reactor.listenTCP(6123, factory)
reactor.run()

