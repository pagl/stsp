from __future__ import absolute_import
from __future__ import print_function

import matplotlib.pyplot as plt
import numpy as np
import os
import pandas as pd
import seaborn as sns
import shutil
import subprocess

from argparse import ArgumentParser
from collections import defaultdict, namedtuple
from datetime import datetime
from glob import glob

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
                   "final_score", "iterations", "steps", "run_time"]

ALGORITHMS = ["random", "heuristic", "greedy", "steepest"]
EXPERIMENT_MAX_TIME = 1000
EXPERIMENT_MAX_ITER = 5000


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
            data_points.append([algorithm, instance] + row.split(","))
        data_points.append(["optimal", instance, "-1", "-1", data[-1], "-1", "-1", "-1"])
    if clear_dir:
        clear_directory(OUTPUT_DIR)
    open(os.path.join(OUTPUT_DIR, AGG_DATA_FILE), "w").write("\n".join(map(",".join, data_points)))


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


def save_figure(name):
    plt.savefig(os.path.join(GRAPH_DIR, name))


def get_facet_grid(data):
    grid = sns.FacetGrid(data, size=5, col="instance", hue="instance",
                         col_wrap=4, margin_titles=True, palette="deep",
                         sharex=False, sharey=False)
    grid.fig.tight_layout(w_pad=3)
    return grid


def algorithm_score_comparison(data):
    data_min = data[data.groupby(["algorithm", "instance"])["final_score"].transform(min) == data["final_score"]]
    data_max = data[data.groupby(["algorithm", "instance"])["final_score"].transform(max) == data["final_score"]]
    data_no_random = data[data.algorithm != "random"]

    # Lowest Score (Bar Plot)
    get_facet_grid(data_min).map(sns.barplot, "algorithm", "final_score",
                                 order=["random", "heuristic", "greedy", "steepest", "optimal"], palette=sns.color_palette("hls", 6))
    save_figure("algorithm_score_comparison_bar_min.png")
    # Highest Score (Bar Plot)
    get_facet_grid(data_max).map(sns.barplot, "algorithm", "final_score",
                                 order=["random", "heuristic", "greedy", "steepest", "optimal"], palette=sns.color_palette("hls", 6))
    save_figure("algorithm_score_comparison_bar_max.png")
    # Average Score (Bar Plot)
    get_facet_grid(data).map(sns.barplot, "algorithm", "final_score", ci="sd", capsize=.05,
                             order=["random", "heuristic", "greedy", "steepest", "optimal"],
                             palette=sns.color_palette("hls", 6))
    save_figure("algorithm_score_comparison_bar_avg.png")
    # Letter Value Plot (without random algorithm)
    get_facet_grid(data_no_random).map(sns.lvplot, "algorithm", "final_score",
                                       order=["heuristic", "greedy", "steepest", "optimal"],
                                       palette=sns.color_palette("hls", 4))
    save_figure("algorithm_score_comparison_letval.png")
    # Violin Plot (without random algorithm)
    get_facet_grid(data_no_random).map(sns.violinplot, "algorithm", "final_score",
                                       order=["heuristic", "greedy", "steepest", "optimal"],
                                       palette=sns.color_palette("hls", 4))
    save_figure("algorithm_score_comparison_violin.png")


def generate_graphs():
    clear_directory(GRAPH_DIR)
    data = pd.read_csv(os.path.join(OUTPUT_DIR, AGG_DATA_FILE))
    algorithm_score_comparison(data)


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
