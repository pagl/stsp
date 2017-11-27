from __future__ import absolute_import
from __future__ import print_function

import numpy as np
import os
import pandas as pd
import shutil
import subprocess

from argparse import ArgumentParser
from collections import defaultdict, namedtuple
from datetime import datetime
from glob import glob

from graphs import *

"""
This app is used to run a set of automatic experiments and generate graphs.

External dependencies:
    - NumPy
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
AGG_DATA_FIELDS = ["algorithm", "instance", "run_id", "init_score",
                   "final_score", "best_so_far", "iterations",
                   "steps", "run_time", "similarity", "quality", "cost"]

ALGORITHMS = ["random", "heuristic", "greedy", "steepest", "tabusearch"]
EXPERIMENT_MAX_TIME = 5000
EXPERIMENT_MIN_ITER = 10
EXPERIMENT_MAX_ITER = 400


def clear_directory(directory):
    if os.path.exists(directory):
        shutil.rmtree(directory)
    os.makedirs(directory)


def aggregate_results(clear_dir=True):
    data_points = [AGG_DATA_FIELDS]
    for filepath in glob(os.path.join(OUTPUT_DIR, "*.res")):
        instance, algorithm = os.path.split(filepath)[-1].rstrip(".res").split("_")
        data = open(filepath).read().strip().split("\n")
        for row in data[:-1]:
            row = row.split(",")
            cost = float(row[6]) / float(row[2])
            quality = 1 / cost
            data_points.append([algorithm, instance] + row + [str(quality), str(cost)])
        data_points.append(["optimal", instance, "-1", "-1", data[-1], "-1", "-1", "-1", "-1", "-1", "-1", "-1"])
    if clear_dir:
        clear_directory(OUTPUT_DIR)
    open(os.path.join(OUTPUT_DIR, AGG_DATA_FILE), "w").write("\n".join(map(",".join, data_points)))


def run_experiment(datafile, max_time, min_iter, max_iter, algorithm, output):
    subprocess.call(["java", "-cp", APP_PATH, MAIN_CLASS,
                     "-d", datafile,
                     "-o", output,
                     "-t", str(max_time),
                     "-i", str(min_iter),
                     "-I", str(max_iter),
                     "-s", algorithm])


def generate_results():
    clear_directory(OUTPUT_DIR)

    for datafile in glob(os.path.join(DATA_DIR, "*.tsp")):
        print("Data: {}".format(datafile))
        for algorithm in ALGORITHMS:
            print("    {:<20}{}".format(algorithm, datetime.now()))
            run_experiment(datafile=datafile.rstrip(".tsp"),
                           max_time=EXPERIMENT_MAX_TIME,
                           min_iter=EXPERIMENT_MIN_ITER,
                           max_iter=EXPERIMENT_MAX_ITER,
                           algorithm=algorithm,
                           output=os.path.join(OUTPUT_DIR,
                                               "{}_{}".format(os.path.split(datafile)[-1]
                                                              .rstrip(".tsp"), algorithm)))
    aggregate_results()


def generate_graphs():
    data = pd.read_csv(os.path.join(OUTPUT_DIR, AGG_DATA_FILE))
    clear_directory(GRAPH_DIR)

    score_comparison(data, GRAPH_DIR)
    time_comparison(data, GRAPH_DIR)
    steps_comparison(data, GRAPH_DIR)
    quality_comparison(data, GRAPH_DIR)
    init_vs_final_score(data, GRAPH_DIR)
    multi_start_score(data, GRAPH_DIR)
    similarity_comparision(data, GRAPH_DIR)


def parse_args():
    parser = ArgumentParser()
    parser.add_argument("--results", action="store_true")
    parser.add_argument("--graphs", action="store_true")
    return parser.parse_args()


if __name__ == "__main__":
    args = parse_args()
    if args.results:
        generate_results()
    if args.graphs:
        generate_graphs()
