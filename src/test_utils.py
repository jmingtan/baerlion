import os
import os.path
import time

from nose import with_setup
import utils

TEST_FILE = "SAMPLE_XYZ.json"
TEST_PATH = os.path.join(utils.DATA_DIR, TEST_FILE)
TEST_DATA = '["sample data"]'
TEST_EXPECTED = ["sample data"]

def setup():
    f = open(TEST_PATH, 'w')
    f.write(str(TEST_DATA))
    f.close()

def teardown():
    if os.path.exists(TEST_PATH):
        os.remove(TEST_PATH)

@with_setup(setup, teardown)
def test_find_dir():
    assert utils.find_dir(utils.DATA_DIR) != None

@with_setup(setup, teardown)
def test_load_data():
    assert utils.load_data(TEST_FILE) == TEST_EXPECTED

def test_wait():
    interval = 1
    start = int(time.time())
    utils.wait(interval)
    assert int(time.time()) - start == interval

