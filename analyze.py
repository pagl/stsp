from __future__ import absolute_import
from __future__ import print_function

import os
import shutil
import pandas as pd
import seaborn as sns
import subprocess

from datetime import datetime
from glob import glob

"""
This app is used to run a set of automatic experiments and generate graphs.

External dependencies:
    - Pandas
    - Seaborn
"""

APP_NAME = "STSP-1.0"
APP_PATH = os.path.join("target", "{}.jar".format(APP_NAME))
MAIN_CLASS = "stsp.Main"

DATA_FOLDER = "res"
OUTPUT_FOLDER = os.path.join("docs", "results")

ALGORITHMS = ["random", "heuristic", "greedy", "steepest"]
EXPERIMENT_MAX_TIME = 1000
EXPERIMENT_MAX_ITER = 5000


def clear_directory(directory):
    if os.path.exists(directory):
        shutil.rmtree(directory)
    os.makedirs(directory)


def run_experiment(datafile, max_time, max_iter, algorithm, output):
    subprocess.call(["java", "-cp", APP_PATH, MAIN_CLASS,
                     "-d", datafile,
                     "-o", output,
                     "-t", str(max_time),
                     "-i", str(max_iter),
                     "-s", algorithm])


def generate_results():
    clear_directory(OUTPUT_FOLDER)

    for datafile in glob(os.path.join(DATA_FOLDER, "*.tsp")):
        print("Data: {}".format(datafile))
        for algorithm in ALGORITHMS:
            print("    {:<20}{}".format(algorithm, datetime.now()))
            run_experiment(datafile=datafile.rstrip(".tsp"),
                           max_time=EXPERIMENT_MAX_TIME,
                           max_iter=EXPERIMENT_MAX_ITER,
                           algorithm=algorithm,
                           output=os.path.join(OUTPUT_FOLDER,
                                               "{}_{}".format(os.path.split(datafile)[-1]
                                                              .rstrip(".tsp"), algorithm)))

if __name__ == "__main__":
    generate_results()
