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
 * CSC369 Assignment 1 - a1fs types, constants, and data structures header file.
 */

#pragma once

#include <assert.h>
#include <stdint.h>
#include <limits.h>
#include <sys/stat.h>
#include <time.h>
#include <string.h>
#include <stdlib.h>

/**
 * a1fs block size in bytes. You are not allowed to change this value.
 *
 * The block size is the unit of space allocation. Each file (and directory)
 * must occupy an integral number of blocks. Each of the file systems metadata
 * partitions, e.g. superblock, inode/block bitmaps, inode table (but not an
 * individual inode) must also occupy an integral number of blocks.
 */
#define A1FS_BLOCK_SIZE 4096

/** Maximum file name (path component) length. Includes the null terminator. */
#define A1FS_NAME_MAX 252

/** Maximum file path length. Includes the null terminator. */
#define A1FS_PATH_MAX PATH_MAX

/** Block number (block pointer) type. */
typedef uint32_t a1fs_blk_t;

/** Inode number type. */
typedef uint32_t a1fs_ino_t;

/** Fixed size directory entry structure. */
typedef struct a1fs_dentry {
	/** Inode number. */
	a1fs_ino_t ino;
	/** File name. A null-terminated string. */
	char name[A1FS_NAME_MAX];

} a1fs_dentry;
// total size: 256


/** Magic value that can be used to identify an a1fs image. */
#define A1FS_MAGIC 0xC5C369A1C5C369A1ul

/** a1fs superblock. */
typedef struct a1fs_superblock {
	/** Must match A1FS_MAGIC. */
	uint64_t magic;
	/** File system size in bytes. */
	uint64_t size;

	//TODO
	unsigned int   s_inodes_count;              /* Inodes count */
	unsigned int   s_blocks_count;              /* Blocks count */
	unsigned int   s_free_blocks_count;         /* Free blocks count */
	unsigned int   s_free_inodes_count;         /* Free inodes count */
	unsigned int   s_inode_bitmap_addr;         /* Starting block of inode bitmap*/
	unsigned int   s_inode_table_addr;          /* Starting block of inode table*/
	unsigned int   s_data_bitmap_addr;          /* Starting block of data block bitmap*/
	unsigned int   s_data_blocks_addr;          /* Starting block of data blocks*/
	
	unsigned int   s_next_free_data_block_addr; /* the index after the last used data block */
	unsigned int   s_next_free_inode_addr; /* the index after the last used inode */

} a1fs_superblock;

// Superblock must fit into a single block
static_assert(sizeof(a1fs_superblock) <= A1FS_BLOCK_SIZE,
              "superblock is too large");


/** Extent - a contiguous range of blocks. */
typedef struct a1fs_extent {
	/** Starting block of the extent. */
	a1fs_blk_t start;
	/** Number of blocks in the extent. */
	a1fs_blk_t count;
	// total: 8 bytes

} a1fs_extent;


/** a1fs inode. */
typedef struct a1fs_inode {
	/** File mode. */
	mode_t mode;
	/** Reference count (number of hard links). */
	uint32_t links;
	/** File size in bytes. */
	uint64_t size;

	/**
	 * Last modification timestamp.
	 *
	 * Use the CLOCK_REALTIME clock; see "man 3 clock_gettime". Must be updated
	 * when the file (or directory) is created, written to, or its size changes.
	 */
	struct timespec mtime;

	//TODO
	struct timespec atime;       /* Access time */
	struct timespec ctime;       /* Creation time */
	struct timespec dtime;       /* Deletion Time (delete any element) */
	unsigned int   extent_table_addr; /* address for the data block which holds the extent table for this inode */
	short extent_count;			  /* Extent Count*/
	int   padding[9];			  /* padding */
	// total: 128 Byte

	//NOTE: You might have to add padding (e.g. a dummy char array field) at the
	// end of the struct in order to satisfy the assertion below. Try to keep
	// the size of this struct minimal, but don't worry about the "wasted space"
	// introduced by the required padding.

} a1fs_inode;

// A single block must fit an integral number of inodes
static_assert(A1FS_BLOCK_SIZE % sizeof(a1fs_inode) == 0, "invalid inode size");


static_assert(sizeof(a1fs_dentry) == 256, "invalid dentry size");
