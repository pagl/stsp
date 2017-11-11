from __future__ import absolute_import
from __future__ import print_function

import matplotlib.pyplot as plt
import os
import pandas as pd
import seaborn as sns
import shutil
import subprocess

from collections import defaultdict, namedtuple
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

DATA_DIR = "res"
OUTPUT_DIR = os.path.join("docs", "results")
GRAPH_DIR = os.path.join("docs", "graphs")
AGG_DATA_FILE = "agg_data.csv"
AGG_DATA_FIELDS = ["algorithm", "datafile", "run_id", "init_score",
                   "final_score", "iterations", "steps", "run_time"]
OPT_SCORE_FILE = "opt_score.csv"

ALGORITHMS = ["random", "heuristic", "greedy", "steepest"]
EXPERIMENT_MAX_TIME = 1000
EXPERIMENT_MAX_ITER = 5000


def clear_directory(directory):
    if os.path.exists(directory):
        shutil.rmtree(directory)
    os.makedirs(directory)


def aggregate_results(clear_dir=True):
    opt_scores, data_points = list(), [AGG_DATA_FIELDS]
    for filepath in glob(os.path.join(OUTPUT_DIR, "*.res")):
        datafile, algorithm = os.path.split(filepath)[-1].rstrip(".res").split("_")
        data = open(filepath).read().strip().split("\n")
        for row in data[:-1]:
            data_points.append([algorithm, datafile] + row.split(","))
        opt_scores.append([datafile, algorithm, data[-1]])
    if clear_dir:
        clear_directory(OUTPUT_DIR)
    open(os.path.join(OUTPUT_DIR, AGG_DATA_FILE), "w").write("\n".join(map(",".join, data_points)))
    open(os.path.join(OUTPUT_DIR, OPT_SCORE_FILE), "w").write("\n".join(map(",".join, opt_scores)))


def run_experiment(datafile, max_time, max_iter, algorithm, output):
    subprocess.call(["java", "-cp", APP_PATH, MAIN_CLASS,
                     "-d", datafile,
                     "-o", output,
                     "-t", str(max_time),
                     "-i", str(max_iter),
                     "-s", algorithm])


def generate_results():
    clear_directory(OUTPUT_DIR)

    for datafile in glob(os.path.join(DATA_DIR, "*.tsp")):
        print("Data: {}".format(datafile))
        for algorithm in ALGORITHMS:
            print("    {:<20}{}".format(algorithm, datetime.now()))
            run_experiment(datafile=datafile.rstrip(".tsp"),
                           max_time=EXPERIMENT_MAX_TIME,
                           max_iter=EXPERIMENT_MAX_ITER,
                           algorithm=algorithm,
                           output=os.path.join(OUTPUT_DIR,
                                               "{}_{}".format(os.path.split(datafile)[-1]
                                                              .rstrip(".tsp"), algorithm)))
    aggregate_results()


if __name__ == "__main__":
    generate_results()
