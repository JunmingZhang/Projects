# STA304_project repo guidance

This README introduces all work for this project, including RMarkdown, code, report and datasets. And here is a bullet list
which describes in the repo, and what you need to notice.

**Note: in order to read and write the datasets in your device, remember to change the path when reading and writing datasets for all scripts and Rmarkdown!**

* License: an MIT license of my work
* data.zip: a compressed folder contains all datasets for this project. Please unzip the file to obtain the datasets. In this folder, both raw data (8 tables) and the processed datasets, i.e., train set and test set (***train.csv*** and ***test.csv***)
* datagen.R: an R script to process the raw data, including computing the metrics we need, and combine the raw datasets and split the train and test dataset. It also can plot the data in the train set and test set.
* model.R: an R script to build the models and make diagnosis on models, the Rmarkdown uses the code in this file to generate the result and simulation we include in the report.
* report.Rmd: generate the report for the whole project.
* report.pdf: the report of this project generated by report.Rmd