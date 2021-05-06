# small script for debugging puerpose
# for demo, please run runit.sh

mkdir /tmp/mnt
truncate -s 64K img
./mkfs.a1fs -i 16 img
gdb --args ./a1fs img /mnt -d

