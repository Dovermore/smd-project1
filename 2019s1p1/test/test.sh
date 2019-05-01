#!/usr/bin/env bash

# Assume the directory is at project level root

sample_dir=./test/
output_prefix=out_
sample_prefix=sample_
postfix=.txt

for subscript in {1..6}; do
    sample_file=$sample_dir$sample_prefix$subscript$postfix
    output_file=$sample_dir$output_prefix$subscript$postfix
    diff $output_file $sample_file
done