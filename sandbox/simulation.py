import time
import utils

class Data:
	"""Represents a class backed by a json data file

	Loads a JSON object referenced by the 'filename' class attribute
	into the 'data' class attribute. The attributes for this base class
	are empty and must be subclassed to have any use.

	>>> Data.filename
	''
	>>> Data.data
	"""
	filename = ''
	data = None

	def __load__(self, file=None):
		"""Loads JSON data from file
		
		>>> testname = 'simulation.json'
		>>> Data().__load__(testname)
		>>> Data.data
		['sample data']
		>>> Data.filename == testname
		True
		"""
		if file is None:
			file = self.__class__.filename
		if len(file) > 0:
			self.__class__.data = utils.loadData(file)
			self.__class__.filename = file

class Simulation(Data):
	filename = 'simulation.json'

	def step(self):
		"""Runs one step of the simulation"""
		print "Step"
		
class BaerlionGame(object):
	def __init__(self):
		self.sim = Simulation()
		
	def run(self):
		interval = 2
		while True:
			start = time.time()
			while time.time() - start < interval:
				pass
			self.update()
	
	def update(self):
		self.sim.step()

if __name__ == '__main__':
	BaerlionGame().run()
