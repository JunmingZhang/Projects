#!/bin/bash

path="/tmp/mnt"
set -x
# compile the codes
make

echo "prepare for running, including creating the image, formating the image and mount the image"
# create an image for the file system
truncate -s 1M img
# make a mount point
mkdir /tmp/mnt
# format the image
./mkfs.a1fs -f -i 128 img
# mount the image
./a1fs img /tmp/mnt

echo "the property of the file system"
# show the stats of the file system
df /tmp/mnt
echo "properties of the root directory"
# show stats of the root directory
stat /tmp/mnt

echo "play around our file system"
# make some files and directories
mkdir "$path"/a
mkdir "$path"/b
mkdir "$path"/c
touch "$path"/a.txt
touch "$path"/b.txt
touch "$path"/c.txt

# show contents in the file system and the properties
ls $path -la

# delete some directories
rmdir "$path"/a
rmdir "$path"/c

# delete some files
rm "$path"/b.txt
rm "$path"/c.txt

# show the contents left
ls $path -la

# change name of the directory
mv "$path"/b "$path"/a

touch "$path"/b.txt
# change the name of the file with existing name
mv "$path"/a.txt "$path"/b.txt

ls $path -la

# change to a non-existing name
mv "$path"/b.txt "$path"/a.txt

ls $path -la 

# write something in a.txt
echo "hello world!" > "$path"/a.txt

# read a.txt
cat "$path"/a.txt

# truncate to different sizes and read
truncate -s 5K "$path"/a.txt
ls -la "$path"/a.txt
cat "$path"/a.txt
truncate -s 3K "$path"/a.txt
ls -la "$path"/a.txt
cat "$path"/a.txt
truncate -s 20 "$path"/a.txt
ls -la "$path"/a.txt
cat "$path"/a.txt
truncate -s 20 "$path"/a.txt
ls -la "$path"/a.txt
cat "$path"/a.txt
truncate -s 0 "$path"/a.txt
ls -la "$path"/a.txt
cat "$path"/a.txt
truncate -s 16 "$path"/a.txt
ls -la "path"/a.txt
cat "$path"/a.txt

# show operations in a sub directory
touch "$path"/a/b.txt
echo  "THIS IS A FILE SYSTEM!" > b.txt
mv "$path"/a/b.txt "$path"/a/c.txt
cat "$path"/a/c.txt
mkdir "$path"/a/d
ls -la $path

echo "unmount the image"
# unmount the image
fusermount -u /tmp/mnt

echo "mount the file system again"
./a1fs img $path

# show the contents not lost
df $path
echo ""
stat $path
echo ""
ls -la $path
ls -la $path/a

echo "end of demo"
# clean up spaces
fusermount -u $path
rmdir $path
make clean
rm -f img
