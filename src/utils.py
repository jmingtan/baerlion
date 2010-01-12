import json
import os
import os.path
import time

DATA_DIR = "data"

def find_dir(name):
    """Recursively search parent directories until a match is found

    Returns absolute pathname of the directory, None otherwise.
    Stops when the root directory has been reached.

    >>> find_dir('data')
    'c:/baerlion/data'
    """
    cwd = os.getcwd()
    while len(os.path.split(cwd)[1]) != 0:
        path = os.path.join(cwd, name)
        if os.path.exists(path):
            return path
        cwd = os.path.split(cwd)[0]

def load_data(filename):
    """Maps a filename to the data directory and loads it as JSON data

    >>> load_data('simulation.json')
    ['sample data']
    """
    file = os.path.join(find_dir(DATA_DIR), filename)
    with open(file) as f:
        return json.loads(f.read())

def wait(interval):
    """Waits for a specified interval

    >>> wait(4)
    """
    start = time.time()
    while time.time() - start < interval:
        pass

if __name__ == '__main__':
    import doctest
    doctest.testmod()

