/*
 * This code is provided solely for the personal and private use of students
 * taking the CSC369H course at the University of Toronto. Copying for purposes
 * other than this use is expressly prohibited. All forms of distribution of
 * this code, including but not limited to public repositories on GitHub,
 * GitLab, Bitbucket, or any other online platform, whether as given or with
 * any changes, are expressly prohibited.
 *
 * Authors: Alexey Khrabrov, Karen Reid
 *
 * All of the files in this directory and all subdirectories are:
 * Copyright (c) 2019 Karen Reid
 */

/**
 * CSC369 Assignment 1 - a1fs formatting tool.
 */

#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/mman.h>
#include <unistd.h>

#include "a1fs.h"
#include "map.h"
#include "helpers.h"

/** Command line options. */
typedef struct mkfs_opts {
	/** File system image file path. */
	const char *img_path;
	/** Number of inodes. */
	size_t n_inodes;

	/** Print help and exit. */
	bool help;
	/** Overwrite existing file system. */
	bool force;
	/** Sync memory-mapped image file contents to disk. */
	bool sync;
	/** Verbose output. If false, the program must only print errors. */
	bool verbose;
	/** Zero out image contents. */
	bool zero;

} mkfs_opts;

static const char *help_str = "\
Usage: %s options image\n\
\n\
Format the image file into a1fs file system. The file must exist and\n\
its size must be a multiple of a1fs block size - %zu bytes.\n\
\n\
Options:\n\
    -i num  number of inodes; required argument\n\
    -h      print help and exit\n\
    -f      force format - overwrite existing a1fs file system\n\
    -s      sync image file contents to disk\n\
    -v      verbose output\n\
    -z      zero out image contents\n\
";

static void print_help(FILE *f, const char *progname)
{
	fprintf(f, help_str, progname, A1FS_BLOCK_SIZE);
}


static bool parse_args(int argc, char *argv[], mkfs_opts *opts)
{
	char o;
	while ((o = getopt(argc, argv, "i:hfsvz")) != -1) {
		switch (o) {
			case 'i': opts->n_inodes = strtoul(optarg, NULL, 10); break;

			case 'h': opts->help    = true; return true;// skip other arguments
			case 'f': opts->force   = true; break;
			case 's': opts->sync    = true; break;
			case 'v': opts->verbose = true; break;
			case 'z': opts->zero    = true; break;

			case '?': return false;
			default : assert(false);
		}
	}

	if (optind >= argc) {
		fprintf(stderr, "Missing image path\n");
		return false;
	}
	opts->img_path = argv[optind];

	if (opts->n_inodes == 0) {
		fprintf(stderr, "Missing or invalid number of inodes\n");
		return false;
	}
	return true;
}


/** Determine if the image has already been formatted into a1fs. */
static bool a1fs_is_present(void *image)
{
	//TODO: check if the image already contains a valid a1fs superblock
	a1fs_superblock *sb = (a1fs_superblock *)image;
	return sb->magic == A1FS_MAGIC;
}

/**
 * Format the image into a1fs.
 *
 * NOTE: Must update mtime of the root directory.
 *
 * @param image  pointer to the start of the image.
 * @param size   image size in bytes.
 * @param opts   command line options.
 * @return       true on success;
 *               false on error, e.g. options are invalid for given image size.
 */
static bool mkfs(void *image, size_t size, mkfs_opts *opts)
{
	//TODO: initialize the superblock and create an empty root directory
	// (void)image;
	// (void)size;
	// (void)opts;
	memset(image, 0, size);
	
	// compute the number of blocks for inode bitmap, inode table, data block bitmap and remaining blocks for file/directory
	int n_ib = cei((double)opts->n_inodes / (8 * A1FS_BLOCK_SIZE)); // the number of blocks of inode bitmap
	int n_i = cei((double)(opts->n_inodes * sizeof(a1fs_inode)) / A1FS_BLOCK_SIZE); // the number of blocks for inode table
	int n_left = size / A1FS_BLOCK_SIZE - 1 - n_ib - n_i;
	int n_db = cei((double)n_left / (1 + A1FS_BLOCK_SIZE * 8)); // the number of blocks for data block bitmap
	int n_d = (int) n_left - n_db; // the number of blocks for data block

	// format the superblock for the file system
	a1fs_superblock *sb = (a1fs_superblock *)image;
	sb->magic = A1FS_MAGIC;
	sb->size = size; // the size of the file system
	sb->s_inodes_count = opts->n_inodes; // the number of inodes in the file system
	sb->s_blocks_count = n_d; // the number of data blocks for file and directory
	sb->s_free_inodes_count = opts->n_inodes; // the number of free inodes in the file system
	sb->s_free_blocks_count = n_d; // the number of free data blcoks for file and directory
	sb->s_inode_bitmap_addr = 1; // superblock occupies 1 block (at the beginning of the file system)
	sb->s_data_bitmap_addr = sb->s_inode_bitmap_addr + n_ib; // the address of data block bitmap
	sb->s_inode_table_addr = sb->s_data_bitmap_addr + n_db; // the address of the inode table
	sb->s_data_blocks_addr = sb->s_inode_table_addr + n_i; // the starting address of data blocks
	sb->s_next_free_data_block_addr = sb->s_data_blocks_addr; // the address of the first free data block
	// the address of the first free inode in the inode table
	// the index of inode, not the index of data block
	sb->s_next_free_inode_addr = 0;

	// format the inode for the root directory
	a1fs_inode *root = (a1fs_inode *)((unsigned char*)image + A1FS_BLOCK_SIZE * sb->s_inode_table_addr);
	root->mode = S_IFDIR | 0777;
	root->links = 0;
	root->size = 0;
	clock_gettime(CLOCK_REALTIME, &(root->ctime));
	clock_gettime(CLOCK_REALTIME, &(root->mtime));
	clock_gettime(CLOCK_REALTIME, &(root->atime));
	root->dtime = (struct timespec){0, 0};
	root->extent_count = 1;
	root->extent_table_addr = sb->s_next_free_data_block_addr;

	// update the superblock for the occurrence of root inode
	sb->s_next_free_data_block_addr += 1;
	sb->s_free_blocks_count -= 1;
	sb->s_next_free_inode_addr += 1;
	sb->s_free_inodes_count -= 1;

	// allocate an extent table for the root directory inode
	a1fs_extent *root_inode_extent = (a1fs_extent *)((unsigned char *)image + A1FS_BLOCK_SIZE * root->extent_table_addr);
	// use the next data block of root inode extent table for the data block of root
	root_inode_extent->start = sb->s_next_free_data_block_addr;
	root_inode_extent->count = 1;
	sb->s_next_free_data_block_addr += 1;
	sb->s_free_blocks_count -= 1;

	// write the two directory dentries for the root directory inode
	a1fs_dentry *first = (a1fs_dentry *)((unsigned char *)image + search_next_dentry(root_inode_extent, num_dentry(root)));
	first->ino = 0;
	memset(first->name, 0, A1FS_NAME_MAX);
	strcpy(first->name, ".");
	root->size += sizeof(a1fs_dentry);
	root->links += 1;

	a1fs_dentry *second = (a1fs_dentry *)((unsigned char *)image + search_next_dentry(root_inode_extent, num_dentry(root)));
	second->ino = 0;
	memset(second->name, 0, A1FS_NAME_MAX);
	strcpy(second->name, "..");
	root->size += sizeof(a1fs_dentry);
	root->links += 1;

	// update the inode and data bitmap for the root inode
	unsigned char *inode_bitmap = (unsigned char*)((unsigned char *)image + A1FS_BLOCK_SIZE * sb->s_inode_bitmap_addr);
	inode_bitmap[0] |= 1 << 7;

	unsigned char* datablock_bitmap = (unsigned char*)((unsigned char *)image + A1FS_BLOCK_SIZE * sb->s_data_bitmap_addr);
	datablock_bitmap[0] |= 1 << 7;
	datablock_bitmap[0] |= 1 << 6;

	return true;
}


int main(int argc, char *argv[])
{
	mkfs_opts opts = {0};// defaults are all 0
	if (!parse_args(argc, argv, &opts)) {
		// Invalid arguments, print help to stderr
		print_help(stderr, argv[0]);
		return 1;
	}
	if (opts.help) {
		// Help requested, print it to stdout
		print_help(stdout, argv[0]);
		return 0;
	}

	// Map image file into memory
	size_t size;
	void *image = map_file(opts.img_path, A1FS_BLOCK_SIZE, &size);
	if (image == NULL) return 1;

	// Check if overwriting existing file system
	int ret = 1;
	if (!opts.force && a1fs_is_present(image)) {
		fprintf(stderr, "Image already contains a1fs; use -f to overwrite\n");
		goto end;
	}

	if (opts.zero) memset(image, 0, size);
	if (!mkfs(image, size, &opts)) {
		fprintf(stderr, "Failed to format the image\n");
		goto end;
	}

	// Sync to disk if requested
	if (opts.sync && (msync(image, size, MS_SYNC) < 0)) {
		perror("msync");
		goto end;
	}

	ret = 0;
end:
	munmap(image, size);
	return ret;
}
