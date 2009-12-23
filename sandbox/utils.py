import json
import os
import os.path

DATA_DIR = "data"

def findDir(name):
	"""Recursively search parent directories until a match is found
	
	Returns absolute pathname of the directory, None otherwise.
	Stops when the root directory has been reached.

	>>> findDir('data')
	'c:/baerlion/data'
	"""
	cwd = os.getcwd()
	while len(os.path.split(cwd)[1]) != 0:
		path = os.path.join(cwd, name)
		if os.path.exists(path):
			return path
		cwd = os.path.split(cwd)[0]

def loadData(filename):
	"""Maps a filename to the data directory and loads it as JSON data
	
	>>> loadData('simulation.json')
	['sample data']
	"""
	file = os.path.join(findDir(DATA_DIR), filename)
	with open(file) as f:
		return json.loads(f.read())

if __name__ == '__main__':
	import doctest
	doctest.testmod()

