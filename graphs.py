from __future__ import absolute_import
from __future__ import print_function

import matplotlib.pyplot as plt
import numpy as np
import os
import seaborn as sns

RANDOM = "random"
HEURISTIC = "heuristic"
GREEDY = "greedy"
STEEPEST = "steepest"
OPTIMAL = "optimal"
ANNEALING = "annealing"
TABUSEARCH = "tabusearch"

order_without_random_and_opt = [HEURISTIC, GREEDY, STEEPEST, TABUSEARCH, ANNEALING]
order_all_with_opt = [RANDOM, HEURISTIC, GREEDY, STEEPEST, TABUSEARCH, ANNEALING, OPTIMAL]
order_all = [RANDOM, HEURISTIC, GREEDY, STEEPEST, TABUSEARCH, ANNEALING]
order_without_random_with_opt = [HEURISTIC, GREEDY, STEEPEST, TABUSEARCH, ANNEALING, OPTIMAL]
order_without_heuristic = [RANDOM, GREEDY, STEEPEST]
order_ls_sa_ts = [GREEDY, STEEPEST, TABUSEARCH, ANNEALING]
order_ls = [GREEDY, STEEPEST]
order_sa_ts = [TABUSEARCH, ANNEALING]


def save_figure(name, output_dir, format="pdf"):
    plt.savefig(os.path.join(output_dir, "{}.{}".format(name, format)), format=format)


def save_data(data, name, output_dir, format="csv"):
    data.to_csv(os.path.join(output_dir, "{}.{}".format(name, format)))


def get_facet_grid(data):
    sns.set_context(context="paper", font_scale=1.7)
    sns.set_style("whitegrid")
    grid = sns.FacetGrid(data, size=5, col="instance", hue="instance",
                         col_wrap=3, margin_titles=True, palette="deep",
                         sharex=False, sharey=False)
    [plt.setp(ax.get_xticklabels(), rotation=30) for ax in grid.axes.flat]
    grid.fig.tight_layout(w_pad=3)
    return grid


def score_comparison(data, output_dir):
    data = data[~data["algorithm"].isin(["random", "optimal"])]
    data_min = data[data.groupby(["algorithm", "instance"])["final_score"].transform(min) == data["final_score"]]
    data_max = data[data.groupby(["algorithm", "instance"])["final_score"].transform(max) == data["final_score"]]

    # Lowest Score (Bar Plot)
    grid = get_facet_grid(data_min)
    grid.map(sns.barplot, "algorithm", "score_dist_ratio",
             order=order_without_random_and_opt,
             palette=sns.light_palette("black", 7))
    grid.set(ylim=(1, None))
    save_figure("score_comparison_bar_min", output_dir)
    save_data(data[["algorithm", "instance", "final_score"]].groupby(["algorithm", "instance"]).min(),
              "score_comparison_bar_min", output_dir)

    # Highest Score (Bar Plot)
    grid = get_facet_grid(data_max)
    grid.map(sns.barplot, "algorithm", "score_dist_ratio",
             order=order_without_random_and_opt,
             palette=sns.light_palette("black", 7))
    grid.set(ylim=(1, None))
    save_figure("score_comparison_bar_max", output_dir)
    save_data(data[["algorithm", "instance", "final_score"]].groupby(["algorithm", "instance"]).max(),
              "score_comparison_bar_max", output_dir)

    # Average Score (Bar Plot)
    grid = get_facet_grid(data)
    grid.map(sns.barplot, "algorithm", "score_dist_ratio", ci="sd", capsize=.05,
             order=order_without_random_and_opt,
             palette=sns.light_palette("black", 7))
    grid.set(ylim=(1, None))
    save_figure("score_comparison_bar_avg", output_dir)
    save_data(data[["algorithm", "instance", "final_score"]].groupby(["algorithm", "instance"]).mean(),
              "score_comparison_bar_avg", output_dir)

    # Score (Letter Value Plot)
    get_facet_grid(data).map(sns.lvplot, "algorithm", "score_dist_ratio",
                             order=order_without_random_and_opt,
                             palette=sns.light_palette("black", 6))
    save_figure("score_comparison_letval", output_dir)

    # Score (Violin Plot)
    get_facet_grid(data).map(sns.violinplot, "algorithm", "score_dist_ratio",
                             order=order_without_random_and_opt,
                             palette=sns.light_palette("black", 6))
    save_figure("score_comparison_violin", output_dir)


def time_comparison(data, output_dir):
    # Average Time (Bar Plot)
    get_facet_grid(data).map(sns.barplot, "algorithm", "run_time", ci="sd", capsize=.05,
                             order=order_all,
                             palette=sns.light_palette("black", 7))
    save_figure("time_comparison_bar_avg", output_dir)

    # Time (Letter Value Plot)
    get_facet_grid(data).map(sns.lvplot, "algorithm", "run_time",
                             order=order_all,
                             palette=sns.light_palette("black", 7))
    save_figure("time_comparison_letval", output_dir)


def steps_comparison(data, output_dir):
    # Average Solutions (Bar Plot)
    grid = get_facet_grid(data)
    grid.map(sns.barplot, "algorithm", "solutions", ci="sd", capsize=.05,
             order=order_ls_sa_ts,
             palette=sns.light_palette("black", 4))
    grid.set(yscale="log")
    save_figure("solutions_comparison_bar_avg", output_dir)

    # Solutions (Violin Plot)
    grid = get_facet_grid(data)
    grid.map(sns.violinplot, "algorithm", "solutions",
             order=order_ls_sa_ts,
             palette=sns.light_palette("black", 4))
    grid.set(yscale="log")
    save_figure("solutions_comparison_violin", output_dir)

    # Average Steps (Bar Plot)
    grid = get_facet_grid(data)
    grid.map(sns.barplot, "algorithm", "steps", ci="sd", capsize=.05,
             order=order_ls_sa_ts,
             palette=sns.light_palette("black", 4))
    grid.set(yscale="log")
    save_figure("steps_comparison_bar_avg", output_dir)

    # Steps (Violin Plot)
    grid = get_facet_grid(data)
    grid.map(sns.violinplot, "algorithm", "steps",
             order=order_ls_sa_ts,
             palette=sns.light_palette("black", 4))
    grid.set(yscale="log")
    save_figure("steps_comparison_violin", output_dir)


def quality_comparison(data, output_dir):
    # Average Quality (Bar Plot)
    grid = get_facet_grid(data)
    grid.map(sns.barplot, "algorithm", "quality", ci="sd", capsize=.05,
             order=order_all,
             palette=sns.light_palette("black", 6))
    grid.set(yscale="log")
    save_figure("quality_comparison_bar_avg", output_dir)

    # Quality (Letter Value Plot)
    grid = get_facet_grid(data)
    grid.map(sns.lvplot, "algorithm", "quality",
             order=order_all,
             palette=sns.light_palette("black", 6))
    grid.set(yscale="log")
    save_figure("quality_comparison_letval", output_dir)

    # Average Cost (Bar Plot)
    get_facet_grid(data).map(sns.barplot, "algorithm", "cost", ci="sd", capsize=.05,
                             order=order_all,
                             palette=sns.light_palette("black", 6))
    save_figure("cost_bar_avg", output_dir)

    # Cost (Letter Value Plot)
    get_facet_grid(data).map(sns.lvplot, "algorithm", "cost",
                             order=order_all,
                             palette=sns.light_palette("black", 6))
    save_figure("cost_comparison_letval", output_dir)


def init_vs_final_score(data, output_dir):
    data = data[data.algorithm.isin(order_ls)]

    grid = get_facet_grid(data)
    grid.map(sns.pointplot, "init_score", "final_score", "algorithm",
             hue_order=order_ls, join=False, markers=["+", "x"],
             palette=sns.dark_palette("white", 3))
    for ax in grid.axes.flat:
        ax.set_xticklabels([int(float(l.get_text())) for l in ax.get_xticklabels()])
        ax.set_xticks(ax.get_xticks()[::len(ax.get_xticks()) / 6])
    save_figure("init_vs_final_score_point", output_dir)

    grid = get_facet_grid(data)
    grid.map(sns.violinplot, "algorithm", "init_score",
             order=order_ls, palette=sns.light_palette("black", 3))
    grid.map(sns.violinplot, "algorithm", "final_score",
             order=order_ls, palette=sns.light_palette("black", 3))
    grid.set(ylabel="score")

    save_figure("init_vs_final_score_violin", output_dir)


def multi_start_score(data, output_dir):
    plt.clf()
    for instance in data.instance.unique():
        instance_data = data[data.instance == instance]
        for algorithm in order_without_heuristic:
            points = instance_data[instance_data.algorithm == algorithm].best_so_far.as_matrix()
            plt.plot(range(len(points)), points, label=algorithm)
        plt.xlabel('run_id')
        plt.ylabel('score')
        plt.legend()
        save_figure("multi_start_score{}".format(instance), output_dir)
        plt.clf()


def similarity_comparision(data, output_dir):
    grid = get_facet_grid(data)
    grid.map(sns.pointplot, "final_score", "similarity", "algorithm",
             hue_order=order_all, join=False, markers=["+", "x", "o", "|", "_", "2"],
             palette=sns.dark_palette("white", 7), dodge=True)
    for ax in grid.axes.flat:
        ax.set_xticklabels([int(float(l.get_text())) for l in ax.get_xticklabels()])
        ax.set_xticks(ax.get_xticks()[::len(ax.get_xticks()) / 6])
    save_figure("similarity_comparision", output_dir)
