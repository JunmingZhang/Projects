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
 * CSC369 Assignment 1 - a1fs driver implementation.
 */

#include <errno.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/mman.h>

// Using 2.9.x FUSE API
#define FUSE_USE_VERSION 29
#include <fuse.h>

#include "a1fs.h"
#include "fs_ctx.h"
#include "options.h"
#include "map.h"
#include "helpers.h"

//NOTE: All path arguments are absolute paths within the a1fs file system and
// start with a '/' that corresponds to the a1fs root directory.
//
// For example, if a1fs is mounted at "~/my_csc369_repo/a1b/mnt/", the path to a
// file at "~/my_csc369_repo/a1b/mnt/dir/file" (as seen by the OS) will be
// passed to FUSE callbacks as "/dir/file".
//
// Paths to directories (except for the root directory - "/") do not end in a
// trailing '/'. For example, "~/my_csc369_repo/a1b/mnt/dir/" will be passed to
// FUSE callbacks as "/dir".


/**
 * Initialize the file system.
 *
 * Called when the file system is mounted. NOTE: we are not using the FUSE
 * init() callback since it doesn't support returning errors. This function must
 * be called explicitly before fuse_main().
 *
 * @param fs    file system context to initialize.
 * @param opts  command line options.
 * @return      true on success; false on failure.
 */
static bool a1fs_init(fs_ctx *fs, a1fs_opts *opts)
{
	// Nothing to initialize if only printing help or version
	if (opts->help || opts->version) return true;

	size_t size;
	void *image = map_file(opts->img_path, A1FS_BLOCK_SIZE, &size);
	if (!image) return false;

	return fs_ctx_init(fs, image, size, opts);
}

/**
 * Cleanup the file system.
 *
 * Called when the file system is unmounted. Must cleanup all the resources
 * created in a1fs_init().
 */
static void a1fs_destroy(void *ctx)
{
	fs_ctx *fs = (fs_ctx*)ctx;
	if (fs->image) {
		if (fs->opts->sync && (msync(fs->image, fs->size, MS_SYNC) < 0)) {
			perror("msync");
		}
		munmap(fs->image, fs->size);
		fs_ctx_destroy(fs);
	}
}

/** Get file system context. */
static fs_ctx *get_fs(void)
{
	return (fs_ctx*)fuse_get_context()->private_data;
}


/**
 * Get file system statistics.
 *
 * Implements the statvfs() system call. See "man 2 statvfs" for details.
 * The f_bfree and f_bavail fields should be set to the same value.
 * The f_ffree and f_favail fields should be set to the same value.
 * The following fields can be ignored: f_fsid, f_flag.
 * All remaining fields are required.
 *
 * @param path  path to any file in the file system. Can be ignored.
 * @param st    pointer to the struct statvfs that receives the result.
 * @return      0 on success; -errno on error.
 */
static int a1fs_statfs(const char *path, struct statvfs *st)
{
	(void)path;// unused
	fs_ctx *fs = get_fs();
	if (!fs) {
		return -ENOSYS;
	}

	memset(st, 0, sizeof(*st));
	st->f_bsize   = A1FS_BLOCK_SIZE;
	st->f_frsize  = A1FS_BLOCK_SIZE;
	//TODO: fill in the rest of required fields based on the information stored
	// in the superblock
	(void)fs;
	st->f_namemax = A1FS_NAME_MAX;

	// get the image and find the superblock
	unsigned char* disk = (unsigned char *)(fs->image);
	a1fs_superblock *sb = (a1fs_superblock *)disk;
	if (!sb) {
		return -ENOSYS;
	}
	// fill the file system info needed for the system call into the struct
	st->f_blocks = cei((double) sb->size / A1FS_BLOCK_SIZE);
	st->f_bfree = sb->s_free_blocks_count;
	st->f_bavail = sb->s_free_blocks_count;
	st->f_files = sb->s_inodes_count;
	st->f_ffree = sb->s_free_inodes_count;
	st->f_favail = sb->s_free_inodes_count;

	// return -ENOSYS;
	return 0;
}

/**
 * Get file or directory attributes.
 *
 * Implements the stat() system call. See "man 2 stat" for details.
 * The following fields can be ignored: st_dev, st_ino, st_uid, st_gid, st_rdev,
 *                                      st_blksize, st_atim, st_ctim.
 * All remaining fields are required.
 *
 * NOTE: the st_blocks field is measured in 512-byte units (disk sectors).
 *
 * Errors:
 *   ENAMETOOLONG  the path or one of its components is too long.
 *   ENOENT        a component of the path does not exist.
 *   ENOTDIR       a component of the path prefix is not a directory.
 *
 * @param path  path to a file or directory.
 * @param st    pointer to the struct stat that receives the result.
 * @return      0 on success; -errno on error;
 */
static int a1fs_getattr(const char *path, struct stat *st)
{
	if (strlen(path) >= A1FS_PATH_MAX) return -ENAMETOOLONG;
	fs_ctx *fs = get_fs();

	memset(st, 0, sizeof(*st));

	//NOTE: This is just a placeholder that allows the file system to be mounted
	// without errors. You should remove this from your implementation.
	// if (strcmp(path, "/") == 0) {
	//NOTE: all the fields set below are required and must be set according
	// to the information stored in the corresponding inode
	// 	st->st_mode = S_IFDIR | 0777;
	// 	st->st_nlink = 2;
	// 	st->st_size = 0;
	// 	st->st_blocks = 0 * A1FS_BLOCK_SIZE / 512;
	// 	st->st_mtim = (struct timespec){0};
	// 	return 0;
	// }

	//TODO: lookup the inode for given path and, if it exists, fill in the
	// required fields based on the information stored in the inode
	// (void)fs;
	// unsigned char *disk = (unsigned char *)(fs->image);

	// get the superblock
	a1fs_superblock *sb = (a1fs_superblock *)(fs->image);

	// get the pieces of path names in order
	path_names *names = break_path_to_names(path);
	if (!names) { return -ENOMEM; }
	// get the inode number and inode
	int inode_no = get_inode_by_fname(names, (fs->image));
	// free the path names
	free_path_names(names);
	if (inode_no == -1) {
		return -ENOENT;
	} else if (inode_no == -2) {
		return -ENOTDIR;
	}
	// find the pointer for the inode needed
	a1fs_inode *inode = find_inode_by_index((fs->image), inode_no, sb);
	clock_gettime(CLOCK_REALTIME, &(inode->atime));

	// fill the info needed of this directory for stat() system call
	st->st_mode = inode->mode;
	st->st_nlink = inode->links;
	st->st_size = inode->size;
	st->st_blocks = cei((double) inode->size / 512);
	st->st_mtim = inode->mtime;
	//return -ENOSYS;
	return 0;
}

/**
 * Read a directory.
 *
 * Implements the readdir() system call. Should call filler() for each directory
 * entry. See fuse.h in libfuse source code for details.
 *
 * Assumptions (already verified by FUSE using getattr() calls):
 *   "path" exists and is a directory.
 *
 * Errors:
 *   ENOMEM  not enough memory (e.g. a filler() call failed).
 *
 * @param path    path to the directory.
 * @param buf     buffer that receives the result.
 * @param filler  function that needs to be called for each directory entry.
 *                Pass 0 as offset (4th argument). 3rd argument can be NULL.
 * @param offset  unused.
 * @param fi      unused.
 * @return        0 on success; -errno on error.
 */
static int a1fs_readdir(const char *path, void *buf, fuse_fill_dir_t filler,
                        off_t offset, struct fuse_file_info *fi)
{
	(void)offset;// unused
	(void)fi;// unused
	fs_ctx *fs = get_fs();

	//NOTE: This is just a placeholder that allows the file system to be mounted
	// without errors. You should remove this from your implementation.
	// if (strcmp(path, "/") == 0) {
	// 	filler(buf, "." , NULL, 0);
	// 	filler(buf, "..", NULL, 0);
	// 	return 0;
	// }

	//TODO: lookup the directory inode for given path and iterate through its
	// directory entries
	// (void)fs;
	// return -ENOSYS;
	// unsigned char *disk = (unsigned char *)(fs->image);
	a1fs_superblock *sb = (a1fs_superblock *)(fs->image);
	path_names *names = break_path_to_names(path);
	if (!names) { return -ENOMEM; }
	int inode_no = get_inode_by_fname(names, (fs->image));
	free_path_names(names);
	a1fs_inode *inode = find_inode_by_index((fs->image), inode_no, sb);

	// call the helper to fill the names of files/directories in the working directory in the buffer
	if (fill_dentry_buf(inode, buf, filler, (fs->image)) != 0 ) { return -ENOMEM; }
	
	return 0;
}


/**
 * Create a directory.
 *
 * Implements the mkdir() system call.
 *
 * NOTE: the mode argument may not have the type specification bits set, i.e.
 * S_ISDIR(mode) can be false. To obtain the correct directory type bits use
 * "mode | S_IFDIR".
 *
 * Assumptions (already verified by FUSE using getattr() calls):
 *   "path" doesn't exist.
 *   The parent directory of "path" exists and is a directory.
 *   "path" and its components are not too long.
 *
 * Errors:
 *   ENOMEM  not enough memory (e.g. a malloc() call failed).
 *   ENOSPC  not enough free space in the file system.
 *
 * @param path  path to the directory to create.
 * @param mode  file mode bits.
 * @return      0 on success; -errno on error.
 */
static int a1fs_mkdir(const char *path, mode_t mode)
{
	fs_ctx *fs = get_fs();

	//TODO: create a directory at given path with given mode
	// (void)path;
	(void)mode;
	// (void)fs;
	// return -ENOSYS;
	
	unsigned char *disk = fs->image;
	a1fs_superblock *sb = (a1fs_superblock *)(disk);
	if (sb->s_free_blocks_count < 2 || sb->s_free_inodes_count == 0) {
		return -ENOSPC;
	}
	// Build the path_names struct
	path_names* names = break_path_to_names(path);
	if (!names) { return -ENOMEM; }
	// Get the target file name
	char* file_name = names->names[names->path_count - 1];
	names->names[names->path_count - 1] = NULL;
	names->path_count--;
	// Get the target inode number associate to the parent dir
	int ino_no = get_inode_by_fname(names,disk);
	a1fs_inode *dir_inode = find_inode_by_index(disk, ino_no, sb);
	// Get the free dentry of the inode
	a1fs_dentry *free_dentry = find_free_dentry(sb, dir_inode, disk);
	if (!free_dentry) {
		free(file_name);
		free_path_names(names);
		return -ENOMEM;
	}
	// Update inode metadata
	dir_inode->size += sizeof(a1fs_dentry);
	dir_inode->links += 1;
	clock_gettime(CLOCK_REALTIME, &(dir_inode->atime));
	clock_gettime(CLOCK_REALTIME, &(dir_inode->mtime));
	// Init the file name
	memset(free_dentry->name, '\0', A1FS_NAME_MAX);
	strcpy(free_dentry->name, file_name);
	// Get a free inode position
	int new_inode_no = search_next_free_inode(disk, sb);
	free_dentry->ino = new_inode_no;
	// Get the inode instance
	a1fs_inode* file_inode = find_inode_by_index(disk, new_inode_no, sb);
	// Update the inode bitmap and sb metadata
	update_inode_bitmap(disk, new_inode_no, 1);
	if (sb->s_next_free_inode_addr < sb->s_inodes_count) { sb->s_next_free_inode_addr++; }
	sb->s_free_inodes_count--;
	// Update inode metadata
	file_inode->mode = mode | S_IFDIR;
	file_inode->links = 1;
	file_inode->size = 0;
	file_inode->extent_count = 1;
	file_inode->extent_table_addr = search_next_free_dlk(disk, sb, 1);
	if ((int) file_inode->extent_table_addr == INT_MAX) {
		free(file_name);
		free_path_names(names);
		return -ENOSPC;
	}
	if (sb->s_next_free_data_block_addr + 1 < sb->s_data_blocks_addr + sb->s_blocks_count) {
		sb->s_next_free_data_block_addr++;
	}
	update_data_bitmap(disk, file_inode->extent_table_addr, 1);
	sb->s_free_blocks_count--;
	// update time data
	clock_gettime(CLOCK_REALTIME, &(file_inode->atime));
	clock_gettime(CLOCK_REALTIME, &(file_inode->ctime));
	clock_gettime(CLOCK_REALTIME, &(file_inode->mtime));
	file_inode->dtime.tv_nsec = 0;
	file_inode->dtime.tv_sec = 0;
	// update the extent
	a1fs_extent *inode_extent = (a1fs_extent *)(disk + A1FS_BLOCK_SIZE * file_inode->extent_table_addr);
	inode_extent->start = search_next_free_dlk(disk, sb, 1);
	if ((int) inode_extent->start == INT_MAX) {
		free(file_name);
		free_path_names(names);
		return -ENOSPC;
	}
	if (sb->s_next_free_data_block_addr + 1 < sb->s_data_blocks_addr + sb->s_blocks_count) {
		sb->s_next_free_data_block_addr += 1;
	}
	inode_extent->count = 1;
	update_data_bitmap(disk, inode_extent->start, 1);
	sb->s_free_blocks_count -= 1;
	// add dentries with name "." and ".." which point to itself (.) and the parent directory (..)
	// add /.
	a1fs_dentry *first = (a1fs_dentry *)(disk + search_next_dentry(inode_extent, num_dentry(file_inode)));
	first->ino = new_inode_no;
	memset(first->name, 0, A1FS_NAME_MAX);
	strcpy(first->name, ".");
	file_inode->size += sizeof(a1fs_dentry);
	file_inode->links += 1;
	// add /..
	a1fs_dentry *second = (a1fs_dentry *)(disk + search_next_dentry(inode_extent, num_dentry(file_inode)));
	second->ino = ino_no;
	memset(second->name, 0, A1FS_NAME_MAX);
	strcpy(second->name, "..");
	file_inode->size += sizeof(a1fs_dentry);
	// free path names
	free(file_name);
	free_path_names(names);
	return 0;
}

/**
 * Remove a directory.
 *
 * Implements the rmdir() system call.
 *
 * Assumptions (already verified by FUSE using getattr() calls):
 *   "path" exists and is a directory.
 *
 * Errors:
 *   ENOTEMPTY  the directory is not empty.
 *
 * @param path  path to the directory to remove.
 * @return      0 on success; -errno on error.
 */
static int a1fs_rmdir(const char *path)
{
	fs_ctx *fs = get_fs();

	//TODO: remove the directory at given path (only if it's empty)
	(void)path;
	(void)fs;
	//return -ENOSYS;
	unsigned char *disk = fs->image;
	a1fs_superblock *sb = (a1fs_superblock *)(disk);
	// build path name struct
	path_names* p_n = break_path_to_names(path);
	if (!p_n) { return -ENOMEM; }
	// current inode number
	int cur_ino_no = get_inode_by_fname(p_n,disk);
	a1fs_inode *cur_dir_inode = find_inode_by_index(disk, cur_ino_no, sb);
	if (cur_dir_inode->links > 2) {
		return -ENOTEMPTY;
	}
	// set as empty inode(by links = 0)
	cur_dir_inode->links = 0;
	// update the path name so that it points to parent dir
	char* d_name = p_n->names[p_n->path_count - 1];
	p_n->names[p_n->path_count - 1] = NULL;
	p_n->path_count--;
	// previous inode number
	int prev_ino_no = get_inode_by_fname(p_n,disk);
	a1fs_inode *prev_dir_inode = find_inode_by_index(disk, prev_ino_no, sb);
	a1fs_dentry *expected_dentry = find_expected_dentry(prev_dir_inode, d_name, disk);
	// Empty the dentry
	memset(expected_dentry, 0, sizeof(a1fs_dentry));
	prev_dir_inode->size -= sizeof(a1fs_dentry);
	prev_dir_inode->links -= 1;
	clock_gettime(CLOCK_REALTIME, &(prev_dir_inode->atime));
	clock_gettime(CLOCK_REALTIME, &(prev_dir_inode->dtime));
	clock_gettime(CLOCK_REALTIME, &(prev_dir_inode->mtime));
	// delete data in the removed directory and remove its inode
	del_contents_inode(sb, cur_dir_inode, disk);
	sb->s_free_inodes_count++;
	update_inode_bitmap(disk, cur_ino_no, 0);
	memset(disk + A1FS_BLOCK_SIZE * sb->s_inode_table_addr + sizeof(a1fs_inode) * cur_ino_no, 0, sizeof(a1fs_inode));
	// free path names
	free_path_names(p_n);
	free(d_name);
	return 0;
}

/**
 * Create a file.
 *
 * Implements the open()/creat() system call.
 *
 * Assumptions (already verified by FUSE using getattr() calls):
 *   "path" doesn't exist.
 *   The parent directory of "path" exists and is a directory.
 *   "path" and its components are not too long.
 *
 * Errors:
 *   ENOMEM  not enough memory (e.g. a malloc() call failed).
 *   ENOSPC  not enough free space in the file system.
 *
 * @param path  path to the file to create.
 * @param mode  file mode bits.
 * @param fi    unused.
 * @return      0 on success; -errno on error.
 */
static int a1fs_create(const char *path, mode_t mode, struct fuse_file_info *fi)
{
	(void)fi;// unused
	assert(S_ISREG(mode));
	fs_ctx *fs = get_fs();

	//TODO: create a file at given path with given mode
	(void)path;
	(void)mode;
	(void)fs;
	// return -ENOSYS;
	unsigned char *disk = fs->image;
	a1fs_superblock *sb = (a1fs_superblock *)(disk);
	if (sb->s_free_blocks_count < 3 || sb->s_free_inodes_count == 0) {
		return -ENOSPC;
	}
	// break path names into pieces by order
	path_names* names = break_path_to_names(path);
	if (!names) { return -ENOMEM; }
	// get the name of the created file and only keep the parent inode
	char* file_name = names->names[names->path_count - 1];
	names->names[names->path_count - 1] = NULL;
	names->path_count--;

	// get the parent inode index and pointer
	int ino_no = get_inode_by_fname(names,disk);
	a1fs_inode *dir_inode = find_inode_by_index(disk, ino_no, sb);
	// find free dentry from the disk
	a1fs_dentry *free_dentry = find_free_dentry(sb, dir_inode, disk);
	if (!free_dentry) {
		free(file_name);
		free_path_names(names);
		return -ENOMEM;
	}
	// the parent inode has one more dentry to store the file
	dir_inode->size += sizeof(a1fs_dentry);
	dir_inode->links += 1;
	clock_gettime(CLOCK_REALTIME, &(dir_inode->atime));
	clock_gettime(CLOCK_REALTIME, &(dir_inode->mtime));
	
	// find a free inode for the new file and
	// record the inode number and the created file name in the new file dentry
	memset(free_dentry->name, '\0', A1FS_NAME_MAX);
	strcpy(free_dentry->name, file_name);
	int new_inode_no = search_next_free_inode(disk, sb);
	free_dentry->ino = new_inode_no;

	// find an inode pointer for the new file and update the file system to show the existence of the file
	a1fs_inode* file_inode = find_inode_by_index(disk, new_inode_no, sb);
	update_inode_bitmap(disk, new_inode_no, 1);
	if (sb->s_next_free_inode_addr < sb->s_inodes_count) { sb->s_next_free_inode_addr++; }
	sb->s_free_inodes_count--;

	// set the relative information of the inode
	file_inode->mode = mode;
	file_inode->links = 1;
	file_inode->size = 0;
	file_inode->extent_count = 1;
	file_inode->extent_table_addr = search_next_free_dlk(disk, sb, 1);
	if ((int) file_inode->extent_table_addr == INT_MAX) {
		free(file_name);
		free_path_names(names);
		return -ENOSPC;
	}
	if (sb->s_next_free_data_block_addr + 1 < sb->s_data_blocks_addr + sb->s_blocks_count) {
		sb->s_next_free_data_block_addr++;
	}
	update_data_bitmap(disk, file_inode->extent_table_addr, 1);
	sb->s_free_blocks_count--;
	clock_gettime(CLOCK_REALTIME, &(file_inode->atime));
	clock_gettime(CLOCK_REALTIME, &(file_inode->ctime));
	clock_gettime(CLOCK_REALTIME, &(file_inode->mtime));
	file_inode->dtime.tv_nsec = 0;
	file_inode->dtime.tv_sec = 0;
	
	// free all allocated info
	free(file_name);
	free_path_names(names);
	return 0;
}

/**
 * Remove a file.
 *
 * Implements the unlink() system call.
 *
 * Assumptions (already verified by FUSE using getattr() calls):
 *   "path" exists and is a file.
 *
 * @param path  path to the file to remove.
 * @return      0 on success; -errno on error.
 */
static int a1fs_unlink(const char *path)
{
	fs_ctx *fs = get_fs();

	//TODO: remove the file at given path
	(void)path;
	(void)fs;
	unsigned char *disk = fs->image;
	a1fs_superblock *sb = (a1fs_superblock *)(disk);
	// build path names
	path_names* p_n = break_path_to_names(path);
	if (!p_n) { return -ENOMEM; }
	// for current inode
	int cur_ino_no = get_inode_by_fname(p_n,disk);
	a1fs_inode *cur_f_inode = find_inode_by_index(disk, cur_ino_no, sb);
	// set as empty inode(by links = 0)
	cur_f_inode->links = 0;
	// get the target file name
	char* d_name = p_n->names[p_n->path_count - 1];
	p_n->names[p_n->path_count - 1] = NULL;
	p_n->path_count--;
	// for previous inode
	int prev_ino_no = get_inode_by_fname(p_n,disk);
	a1fs_inode *prev_dir_inode = find_inode_by_index(disk, prev_ino_no, sb);
	a1fs_dentry *expected_dentry = find_expected_dentry(prev_dir_inode, d_name, disk);
	// empty the dentry
	memset(expected_dentry, 0, sizeof(a1fs_dentry));
	prev_dir_inode->size -= sizeof(a1fs_dentry);
	prev_dir_inode->links -= 1;
	clock_gettime(CLOCK_REALTIME, &(prev_dir_inode->atime));
	clock_gettime(CLOCK_REALTIME, &(prev_dir_inode->dtime));
	clock_gettime(CLOCK_REALTIME, &(prev_dir_inode->mtime));
	// delete data in the removed file and remove its inode
	del_contents_inode(sb, cur_f_inode, disk);
	sb->s_free_inodes_count++;
	update_inode_bitmap(disk, cur_ino_no, 0);
	memset(disk + A1FS_BLOCK_SIZE * sb->s_inode_table_addr + sizeof(a1fs_inode) * cur_ino_no, 0, sizeof(a1fs_inode));
	// free path name
	free_path_names(p_n);
	free(d_name);
	return 0;
}

/**
 * Rename a file or directory.
 *
 * Implements the rename() system call. See "man 2 rename" for details.
 * If the destination file (directory) already exists, it must be replaced with
 * the source file (directory). Existing destination can be replaced if it's a
 * file or an empty directory.
 *
 * Assumptions (already verified by FUSE using getattr() calls):
 *   "from" exists.
 *   The parent directory of "to" exists and is a directory.
 *   If "from" is a file and "to" exists, then "to" is also a file.
 *   If "from" is a directory and "to" exists, then "to" is also a directory.
 *
 * Errors:
 *   ENOMEM     not enough memory (e.g. a malloc() call failed).
 *   ENOTEMPTY  destination is a non-empty directory.
 *   ENOSPC     not enough free space in the file system.
 *
 * @param from  original file path.
 * @param to    new file path.
 * @return      0 on success; -errno on error.
 */
static int a1fs_rename(const char *from, const char *to)
{
	fs_ctx *fs = get_fs();

	//TODO: move the inode (file or directory) at given source path to the
	// destination path, according to the description above
	// (void)from;
	// (void)to;
	// (void)fs;
	// return -ENOSYS;

	// get the disk and the superblock from the image
	unsigned char *disk = (unsigned char *)(fs->image);
	a1fs_superblock *sb = (a1fs_superblock *)(fs->image);

	// break to path names and get the original and new name of the file/directory
	path_names *from_names = break_path_to_names(from);
	if (!from_names) { return -ENOMEM; }
	path_names *to_names = break_path_to_names(to);
	if (!to_names) { return -ENOMEM; }

	// get the new name from the "to" path
	char new_name[strlen(to_names->names[to_names->path_count - 1]) + 1];
	new_name[strlen(to_names->names[to_names->path_count - 1])] = '\0';
	strcpy(new_name, to_names->names[to_names->path_count - 1]);
	// check if there is a file with the new name in the file system
	int to_inode_no = get_inode_by_fname(to_names, disk);
	// leave the path to the parent of the file with new name
	to_names->names[to_names->path_count - 1] = NULL;
	to_names->path_count--;
	int to_parent_inode_no = get_inode_by_fname(to_names, disk);

	// check if there is a file/directory with the new name in the file system
	if (to_inode_no != -1) {
		a1fs_inode *to_inode = find_inode_by_index(disk, to_inode_no, sb);
		// if there is a directory with the new name
		if (S_ISDIR(to_inode->mode)) {
			// raise an error if this directory is not empty
			if (to_inode->size > 2 * sizeof(a1fs_dentry)) {
				free_path_names(from_names);
				free_path_names(to_names);
				return -ENOTEMPTY;
			}
			// else do nothing if the new name is the same as original name or delete the directory if it differs from the original name
			else if (strcmp(from, to) == 0) { goto end; }
			else { a1fs_rmdir(to); }
		}
		// if this is a file, do  nothing if the new name is the same as original name or delete the file if it differs from the original name
		if (S_ISREG(to_inode->mode)) {
			if (strcmp(from, to) == 0) { goto end; }
			else { a1fs_unlink(to); }
		}
	}

	{
		// get the original file/directory name
		char prev_name[strlen(from_names->names[from_names->path_count - 1]) + 1];
		prev_name[strlen(from_names->names[from_names->path_count - 1])] = '\0';
		strcpy(prev_name, from_names->names[from_names->path_count - 1]);
		from_names->names[from_names->path_count - 1] = NULL;
		from_names->path_count--;
		// get the inode of the file/directory with original name
		int from_parent_inode_no = get_inode_by_fname(from_names, disk);
		a1fs_inode *from_parent_inode = find_inode_by_index(disk, from_parent_inode_no, sb);
		// get an empty dentry to store the file with new name
		a1fs_dentry *from_dentry = find_expected_dentry(from_parent_inode, prev_name, disk);

		// separate the inode of file/directory with new name from its original parent directory
		// and update relative info
		int temp = from_dentry->ino;
		memset(from_dentry, 0, sizeof(a1fs_dentry));
		from_parent_inode->size -= sizeof(a1fs_dentry);
		from_parent_inode->links--;
		clock_gettime(CLOCK_REALTIME, &(from_parent_inode->atime));
		clock_gettime(CLOCK_REALTIME, &(from_parent_inode->mtime));
		clock_gettime(CLOCK_REALTIME, &(from_parent_inode->dtime));

		// get the new parent inode and new dnetry in the new parent directory
		// and record (update) the relative info about the new-named file in the new dentry
		a1fs_inode *to_parent_inode = find_inode_by_index(disk, to_parent_inode_no, sb);
		a1fs_dentry *to_dentry = find_free_dentry(sb, to_parent_inode, disk);
		if (!to_dentry) {
			free_path_names(from_names);
			free_path_names(to_names);
			return -ENOSPC;
		}
		to_dentry->ino = temp;
		strcpy(to_dentry->name, new_name);
		to_dentry->name[strlen(new_name)] = '\0';
		to_parent_inode->size += sizeof(a1fs_dentry);
		to_parent_inode->links++;
		clock_gettime(CLOCK_REALTIME, &(to_parent_inode->atime));
		clock_gettime(CLOCK_REALTIME, &(to_parent_inode->mtime));
	}
	
	// clean all allocated space
	end:
		free_path_names(from_names);
		free_path_names(to_names);
		return 0;
}


/**
 * Change the access and modification times of a file or directory.
 *
 * Implements the utimensat() system call. See "man 2 utimensat" for details.
 *
 * NOTE: You only have to implement the setting of modification time (mtime).
 *
 * Assumptions (already verified by FUSE using getattr() calls):
 *   "path" exists.
 *
 * @param path  path to the file or directory.
 * @param tv    timestamps array. See "man 2 utimensat" for details.
 * @return      0 on success; -errno on failure.
 */
static int a1fs_utimens(const char *path, const struct timespec tv[2])
{
	fs_ctx *fs = get_fs();

	//TODO: update the modification timestamp (mtime) in the inode for given
	// path with either the time passed as argument or the current time,
	// according to the utimensat man page
	(void)path;
	(void)tv;
	(void)fs;
	// get the superblock and the path names
	a1fs_superblock *sb = (a1fs_superblock *)(fs->image);
	path_names *names = break_path_to_names(path);
	if (!names) { return -ENOMEM; }

	// get the inode number and pointer to update the inode
	int inode_no = get_inode_by_fname(names, (fs->image));
	free_path_names(names);
	a1fs_inode *inode = find_inode_by_index((fs->image), inode_no, sb);

	if (tv == NULL) { return -EACCES; }

	// update access and modification time of the file/directory
	inode->atime.tv_nsec = tv[0].tv_nsec; inode->atime.tv_sec = tv[0].tv_sec;
	inode->mtime.tv_nsec = tv[1].tv_nsec; inode->mtime.tv_sec = tv[1].tv_sec;
	// return -ENOSYS;
	return 0;
}

/**
 * Change the size of a file.
 *
 * Implements the truncate() system call. Supports both extending and shrinking.
 * If the file is extended, future reads from the new uninitialized range must
 * return ranges filled with zeros.
 *
 * Assumptions (already verified by FUSE using getattr() calls):
 *   "path" exists and is a file.
 *
 * Errors:
 *   ENOMEM  not enough memory (e.g. a malloc() call failed).
 *   ENOSPC  not enough free space in the file system.
 *
 * @param path  path to the file to set the size.
 * @param size  new file size in bytes.
 * @return      0 on success; -errno on error.
 */
static int a1fs_truncate(const char *path, off_t size)
{
	fs_ctx *fs = get_fs();

	//TODO: set new file size, possibly "zeroing out" the uninitialized range
	// (void)path;
	// (void)size;
	// (void)fs;
	// return -ENOSYS;

	// get th disk and the superblock
	unsigned char *disk = (unsigned char *)(fs->image);
	a1fs_superblock *sb = (a1fs_superblock *)disk;

	// break the path names
	path_names *names = break_path_to_names(path);
	if (!names) { return -ENOMEM; }
	// get the inode of the file to be truncated
	int file_no = get_inode_by_fname(names, disk);
	a1fs_inode *file_inode = find_inode_by_index(disk, file_no, sb);
	free_path_names(names);

	// if the size does not change, just return
	if (file_inode->size == (uint64_t)size) { goto end; }
	// if the file is shrinked
	else if (file_inode->size > (uint64_t)size) {
		// find the number of blocks being cancelled
		unsigned int blocks_del_count = cei((double)file_inode->size / A1FS_BLOCK_SIZE) - cei((double)size / A1FS_BLOCK_SIZE);
		if (blocks_del_count == 0) { goto end; }
		// increase the number (== blocks cleared) of free blocks
		sb->s_free_blocks_count += blocks_del_count;
		// shrink the file by calling the helper function
		shrink(disk, file_inode, blocks_del_count);
	// if the file is extended
	} else {
		// find the number of blocks being added
		unsigned int blocks_add_count = cei((double)size / A1FS_BLOCK_SIZE) - cei((double)file_inode->size / A1FS_BLOCK_SIZE);
		if (blocks_add_count == 0) { goto end; }
		if (blocks_add_count > sb->s_free_blocks_count) { return -ENOSPC; }
		if (file_inode->extent_count == 512) { return -ENOMEM; }

		// create new extent for the new added blocks
		// find the start address of new extent for new added blocks
		int start = search_next_free_dlk(disk, sb, blocks_add_count);
		if (start == INT_MAX) { return -ENOSPC; }
		// find a free extent in the extent table
		a1fs_extent* extent = get_free_extent_place(disk, file_inode);
		// record the relative info the extent
		extent->start = start;
		extent->count = blocks_add_count;

		// update the superblock and the inode of the truncated file
		sb->s_free_blocks_count -= extent->count;
		if (sb->s_next_free_data_block_addr + extent->count < sb->s_data_blocks_addr + sb->s_blocks_count) {
			sb->s_next_free_data_block_addr++;
		}
		file_inode->extent_count++;
		for (unsigned int i = 0; i < extent->count; i++) { update_data_bitmap(disk, i + extent->start, 1); }
	}

	end:
		// update the size of the file and relative time
		file_inode->size = size;
		clock_gettime(CLOCK_REALTIME, &(file_inode->atime));
		clock_gettime(CLOCK_REALTIME, &(file_inode->mtime));
		return 0;
}


/**
 * Read data from a file.
 *
 * Implements the pread() system call. Should return exactly the number of bytes
 * requested except on EOF (end of file) or error, otherwise the rest of the
 * data will be substituted with zeros. Reads from file ranges that have not
 * been written to must return ranges filled with zeros.
 *
 * Assumptions (already verified by FUSE using getattr() calls):
 *   "path" exists and is a file.
 *
 * @param path    path to the file to read from.
 * @param buf     pointer to the buffer that receives the data.
 * @param size    buffer size (number of bytes requested).
 * @param offset  offset from the beginning of the file to read from.
 * @param fi      unused.
 * @return        number of bytes read on success; 0 if offset is beyond EOF;
 *                -errno on error.
 */
static int a1fs_read(const char *path, char *buf, size_t size, off_t offset,
                     struct fuse_file_info *fi)
{
	(void)fi;// unused
	fs_ctx *fs = get_fs();

	//TODO: read data from the file at given offset into the buffer
	// (void)path;
	// (void)buf;
	// (void)size;
	// (void)offset;
	// (void)fs;
	// return -ENOSYS;

	// get the disk and the superblock from the formatted image
	unsigned char *disk = (unsigned char *)(fs->image);
	a1fs_superblock *sb = (a1fs_superblock *)disk;
	if (offset >= (int) sb->size) { return 0; }

	// get the breaked path names from path
	path_names *names = break_path_to_names(path);
	if (!names) { return -ENOMEM; }

	// get the inode of the file to be read
	int ino_no = get_inode_by_fname(names, disk);
	a1fs_inode *file_inode = find_inode_by_index(disk, ino_no, sb);
	free_path_names(names);

	// read (number of bytes) from the file
	if (offset >= (int) file_inode->size) { return 0; }
	int read_size = read_all(disk, file_inode, size, offset, buf);
	
	// update the relative time
	clock_gettime(CLOCK_REALTIME, &(file_inode->atime));
	// return the number of bytes read
	return read_size;
}

/**
 * Write data to a file.
 *
 * Implements the pwrite() system call. Should return exactly the number of
 * bytes requested except on error. If the offset is beyond EOF (end of file),
 * the file must be extended. If the write creates a "hole" of uninitialized
 * data, future reads from the "hole" must return ranges filled with zeros.
 *
 * Assumptions (already verified by FUSE using getattr() calls):
 *   "path" exists and is a file.
 *
 * @param path    path to the file to write to.
 * @param buf     pointer to the buffer containing the data.
 * @param size    buffer size (number of bytes requested).
 * @param offset  offset from the beginning of the file to write to.
 * @param fi      unused.
 * @return        number of bytes written on success; -errno on error.
 */
static int a1fs_write(const char *path, const char *buf, size_t size,
                      off_t offset, struct fuse_file_info *fi)
{
	(void)fi;// unused
	fs_ctx *fs = get_fs();

	//TODO: write data from the buffer into the file at given offset, possibly
	// "zeroing out" the uninitialized range
	// (void)path;
	// (void)buf;
	// (void)size;
	// (void)offset;
	// (void)fs;
	//return -ENOSYS;

	unsigned char *disk = (unsigned char *)fs->image;
	a1fs_superblock *sb = (a1fs_superblock *)disk;
	if (sb->size - A1FS_BLOCK_SIZE * (sb->s_data_blocks_addr + 2) < size) { return -ENOSPC; }
	// build path name struct
	path_names *p_n = break_path_to_names(path);
	if (!p_n) { return -ENOMEM; }
	int file_no = get_inode_by_fname(p_n, disk);
	a1fs_inode *file_inode = find_inode_by_index(disk, file_no, sb);
	a1fs_extent *ex_table = (a1fs_extent *)(disk + file_inode->extent_table_addr * A1FS_BLOCK_SIZE);
	// if the inode is empty
	if(file_inode->size == 0) {
		// calculate the block needed for input data
		unsigned int blk_need = cei((double)(size + offset)/A1FS_BLOCK_SIZE);
		if (!check_db_space(disk, blk_need)) {
			free_path_names(p_n);
			return -ENOSPC;
		}
		// for how many blocks to allocate
		unsigned int count = blk_need;
		// for how many blocks left to allocate
		unsigned int blk_left = blk_need;
		// update the inode size
		file_inode->size += size;
		while(count > 0) {
			// find the next free block
			int blk_addr = search_next_free_dlk(disk, sb, count);
			if (blk_addr == INT_MAX) {
				// if can't allocate this size consecutively
				if (count == 1) {
					return -ENOSPC;
				}
				// try size - 1
				count--;
			} 
			else {
				// consecutve size is available
				file_inode->extent_count ++;
				int nx_free_ex_id = get_next_free_extent_id(file_inode, disk);
				if (nx_free_ex_id == -1) {
					return -ENOSPC;
				}
				else {
					// allocate the extent
					ex_table[nx_free_ex_id].start = blk_addr;
					ex_table[nx_free_ex_id].count = count;
					sb->s_next_free_data_block_addr++;
					for (unsigned int k=0; k<count; k++) {
						update_data_bitmap(disk, blk_addr + k, 1);
					}
				}
				// update the control info of the loop
				blk_left = blk_left - count;
				count = blk_left;
			}
		}
	}
	else if(file_inode->size < size + offset) {
		int s = a1fs_truncate(path, (off_t)(size + offset));
		if (s) {
			free_path_names(p_n);
			return s;
		}
	}
	// write process
	//a1fs_extent *file_extent = (a1fs_extent *)(disk + file_inode->extent_table_addr * A1FS_BLOCK_SIZE);
	// unsigned int blk_start = file_extent->start;
	unsigned int blk_offset = offset / A1FS_BLOCK_SIZE;
	int byte_offset = offset % A1FS_BLOCK_SIZE;
	// for indexing to the right file extent
	unsigned int temp = blk_offset;
	int idx = 0;
	while (temp > 0) {
		if(ex_table->count <= temp) {
			int nx_ex_addr = next_nonempty_extent(disk, file_inode, idx);
			ex_table += nx_ex_addr;
			idx += nx_ex_addr;
		}
		temp -= ex_table->count;
	}
	// find where to start writing
	unsigned char *start_byte = (unsigned char *)(disk + ex_table->start * A1FS_BLOCK_SIZE + byte_offset);
	unsigned int count = 0;
	for (unsigned int i=0; i<size; i++) {
		// write the byte one by one
		strncpy((char *)start_byte, buf+i, 1);
		byte_offset ++;
		if (byte_offset == A1FS_BLOCK_SIZE) {
			// if need to jump to next block
			count++;
			byte_offset = 0;
			if (count >= ex_table->count) {
				// if need to jump to nex extent
				int nx_ex_addr = next_nonempty_extent(disk, file_inode, idx);
				ex_table += nx_ex_addr;
				idx += nx_ex_addr;
				start_byte = disk + ex_table->start * A1FS_BLOCK_SIZE;
				count = 0;
			}
		}
		else {
			start_byte++;
		}
	}
	// set time info
	clock_gettime(CLOCK_REALTIME, &(file_inode->atime));
	clock_gettime(CLOCK_REALTIME, &(file_inode->mtime));
	// free path names
	free_path_names(p_n);
	return size;
}


static struct fuse_operations a1fs_ops = {
	.destroy  = a1fs_destroy,
	.statfs   = a1fs_statfs,
	.getattr  = a1fs_getattr,
	.readdir  = a1fs_readdir,
	.mkdir    = a1fs_mkdir,
	.rmdir    = a1fs_rmdir,
	.create   = a1fs_create,
	.unlink   = a1fs_unlink,
	.rename   = a1fs_rename,
	.utimens  = a1fs_utimens,
	.truncate = a1fs_truncate,
	.read     = a1fs_read,
	.write    = a1fs_write,
};

int main(int argc, char *argv[])
{
	a1fs_opts opts = {0};// defaults are all 0
	struct fuse_args args = FUSE_ARGS_INIT(argc, argv);
	if (!a1fs_opt_parse(&args, &opts)) return 1;

	fs_ctx fs = {0};
	if (!a1fs_init(&fs, &opts)) {
		fprintf(stderr, "Failed to mount the file system\n");
		return 1;
	}

	return fuse_main(args.argc, args.argv, &a1fs_ops, &fs);
}
