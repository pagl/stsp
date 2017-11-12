from __future__ import absolute_import
from __future__ import print_function

import matplotlib.pyplot as plt
import numpy as np
import os
import seaborn as sns


def save_figure(name, output_dir, format="pdf"):
    plt.savefig(os.path.join(output_dir, "{}.{}".format(name, format)), format=format)


def get_facet_grid(data):
    grid = sns.FacetGrid(data, size=5, col="instance", hue="instance",
                         col_wrap=4, margin_titles=True, palette="deep",
                         sharex=False, sharey=False)
    grid.fig.tight_layout(w_pad=3)
    return grid


def score_comparison(data, output_dir):
    data_min = data[data.groupby(["algorithm", "instance"])["final_score"].transform(min) == data["final_score"]]
    data_max = data[data.groupby(["algorithm", "instance"])["final_score"].transform(max) == data["final_score"]]

    # Lowest Score (Bar Plot)
    get_facet_grid(data_min).map(sns.barplot, "algorithm", "final_score",
                                 order=["random", "heuristic", "greedy", "steepest", "optimal"],
                                 palette=sns.color_palette("hls", 6))
    save_figure("score_comparison_bar_min", output_dir)

    # Highest Score (Bar Plot)
    get_facet_grid(data_max).map(sns.barplot, "algorithm", "final_score",
                                 order=["random", "heuristic", "greedy", "steepest", "optimal"],
                                 palette=sns.color_palette("hls", 6))
    save_figure("score_comparison_bar_max", output_dir)

    # Average Score (Bar Plot)
    get_facet_grid(data).map(sns.barplot, "algorithm", "final_score", ci="sd", capsize=.05,
                             order=["random", "heuristic", "greedy", "steepest", "optimal"],
                             palette=sns.color_palette("hls", 6))
    save_figure("score_comparison_bar_avg", output_dir)

    # Score (Letter Value Plot)
    get_facet_grid(data).map(sns.lvplot, "algorithm", "final_score",
                             order=["heuristic", "greedy", "steepest", "optimal"],
                             palette=sns.color_palette("hls", 4))
    save_figure("score_comparison_letval", output_dir)

    # Score (Violin Plot)
    get_facet_grid(data).map(sns.violinplot, "algorithm", "final_score",
                             order=["heuristic", "greedy", "steepest", "optimal"],
                             palette=sns.color_palette("hls", 4))
    save_figure("score_comparison_violin", output_dir)


def time_comparison(data, output_dir):
    # Average Time (Bar Plot)
    get_facet_grid(data).map(sns.barplot, "algorithm", "run_time", ci="sd", capsize=.05,
                             order=["random", "heuristic", "greedy", "steepest"],
                             palette=sns.color_palette("hls", 5))
    save_figure("time_comparison_bar_avg", output_dir)

    # Time (Letter Value Plot)
    get_facet_grid(data).map(sns.lvplot, "algorithm", "run_time",
                             order=["random", "heuristic", "greedy", "steepest"],
                             palette=sns.color_palette("hls", 5))
    save_figure("time_comparison_letval", output_dir)


def steps_comparison(data, output_dir):
    # Average Steps (Bar Plot)
    get_facet_grid(data).map(sns.barplot, "algorithm", "steps", ci="sd", capsize=.05,
                             order=["greedy", "steepest"],
                             palette=sns.color_palette("hls", 3))
    save_figure("steps_comparison_bar_avg", output_dir)

    # Steps (Violin Plot)
    get_facet_grid(data).map(sns.violinplot, "algorithm", "steps",
                             order=["greedy", "steepest"],
                             palette=sns.color_palette("hls", 3))
    save_figure("steps_comparison_violin", output_dir)

    # Average Iterations (Bar Plot)
    get_facet_grid(data).map(sns.barplot, "algorithm", "iterations", ci="sd", capsize=.05,
                             order=["greedy", "steepest"],
                             palette=sns.color_palette("hls", 3))
    save_figure("iterations_comparison_bar_avg", output_dir)

    # Iterations (Violin Plot)
    get_facet_grid(data).map(sns.violinplot, "algorithm", "iterations",
                             order=["greedy", "steepest"],
                             palette=sns.color_palette("hls", 3))
    save_figure("iterations_comparison_violin", output_dir)


def quality_comparison(data, output_dir):
    # Average Quality (Bar Plot)
    get_facet_grid(data).map(sns.barplot, "algorithm", "quality", ci="sd", capsize=.05,
                             order=["random", "heuristic", "greedy", "steepest"],
                             palette=sns.color_palette("hls", 5))
    save_figure("quality_comparison_bar_avg", output_dir)

    # Quality (Letter Value Plot)
    get_facet_grid(data).map(sns.lvplot, "algorithm", "quality",
                             order=["random", "heuristic", "greedy", "steepest"],
                             palette=sns.color_palette("hls", 5))
    save_figure("quality_comparison_letval", output_dir)

    # Average Cost (Bar Plot)
    get_facet_grid(data).map(sns.barplot, "algorithm", "cost", ci="sd", capsize=.05,
                             order=["random", "heuristic", "greedy", "steepest"],
                             palette=sns.color_palette("hls", 5))
    save_figure("cost_bar_avg", output_dir)

    # Cost (Letter Value Plot)
    get_facet_grid(data).map(sns.lvplot, "algorithm", "cost",
                             order=["random", "heuristic", "greedy", "steepest"],
                             palette=sns.color_palette("hls", 5))
    save_figure("cost_comparison_letval", output_dir)


def init_vs_final_score(data, output_dir):
    data = data[data.algorithm.isin(["greedy", "steepest"])]

    grid = get_facet_grid(data)
    grid.map(sns.pointplot, "init_score", "final_score", "algorithm",
             hue_order=["greedy", "steepest"], join=False)
    save_figure("init_vs_final_score_point", output_dir)

    grid = get_facet_grid(data)
    grid.map(sns.violinplot, "algorithm", "init_score",
             order=["greedy", "steepest"], palette=sns.color_palette("hls", 3))
    grid.map(sns.violinplot, "algorithm", "final_score",
             order=["greedy", "steepest"], palette="muted")
    grid.set(ylabel="score")

    save_figure("init_vs_final_score_violin", output_dir)


def multi_start_score(data, output_dir):
    for instance in data.instance.unique():
        instance_data = data[data.instance == instance]
        for algorithm in ["random", "greedy", "steepest"]:
            points = instance_data[instance_data.algorithm == algorithm].best_so_far.as_matrix()
            plt.plot(range(len(points)), points, label=algorithm)
        plt.xlabel('run_id')
        plt.ylabel('score')
        plt.legend()
        save_figure("multi_start_score{}".format(instance), output_dir)
        plt.clf()
