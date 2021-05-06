#include <stdbool.h>
#include <stdio.h>

#include "helpers.h"

/**
 * found the number of directory entries an inode have;
 * a directory has at least 1 while a file has no dentry
 * 
 * @param   inode: an a1fs inode
 * @return  the number of dentries a directory has
 */
int num_dentry(a1fs_inode *inode) {
	if (S_ISDIR(inode->mode | S_IFDIR)) {
		return inode->size / sizeof(a1fs_dentry);
	}
	return 0;
}

/**
 * <Peter>
 * check if the dentry is empty
 * 
 * @param    dentry: a1fs directory entry
 * @return   if the dentry is empty or not
 */
bool dentry_isempty(a1fs_dentry *dentry) {
    return (dentry->ino == 0 && strlen(dentry->name) == 0);
}

/**
 * <Peter>
 * return the address of the next free inode
 * 
 * @param   disk: the disk of the file system
 * @param   sb: the superblock of the file system
 * @return  the address of the free inode or INT_MAX if not found
 */
int search_next_free_inode(unsigned char* disk, a1fs_superblock *sb) {
    // if there is no free inodes in the file system
    if (sb->s_free_inodes_count == 0) { return INT_MAX; }
    // if the address of next free inode does not reach the end
    if (sb->s_next_free_inode_addr < sb->s_inodes_count)
	    { return sb->s_next_free_inode_addr; }
    // if all the free inodes have been allocated before (there might be some released after deletion)
    else
    {
        // find the inode bitmap
        unsigned char *bm = disk + sb->s_inode_bitmap_addr * A1FS_BLOCK_SIZE;
        // find free inode from the inode bitmap
        for (int i = 1; i < ((int)sb->s_inodes_count); i++) {
            int i_byte = i / 8;
            int i_bit = 7 - i % 8;
            char in_use = bm[i_byte] & (1 << i_bit);
            if (!in_use) {
                return i;
            }
        }
    }
    return INT_MAX;
}

/**
 * <Peter>
 * find the start address of a sequence of free blocks with length blk_count
 * to form an extent
 * 
 * @param  disk: the disk of the file system
 * @param  sb: superblock of the file system
 * @param  blk_count: the length of free blocks needed for an extent
 * @return the starting address of a sequence of free blocks with length blk_count or INT_MAX if not found
 */
int search_next_free_dlk(unsigned char* disk, a1fs_superblock *sb, unsigned int blk_count) {
    // if the number of free blocks is not enough
    if (blk_count > sb->s_free_blocks_count) {
        return INT_MAX;
    // if not all data blocks have been allocated
    } else if (sb->s_next_free_data_block_addr + blk_count < sb->s_data_blocks_addr + sb->s_blocks_count) {
        return sb->s_next_free_data_block_addr;
    // if all data blocks have been allocated before (there might be blocks freed due to file/directory cancellation)
    } else {
        // the bitmap of data block
        unsigned char *bm = disk + sb->s_data_bitmap_addr * A1FS_BLOCK_SIZE;
        // find if there is a sequence of data blocks unused (with length indicated)
        for (int i = 1; i < ((int)sb->s_blocks_count); i++) {
            unsigned int consecutive_blk_count = 0;
            int i_byte = i / 8;
            int i_bit = 7 - i % 8;
            char in_use = bm[i_byte] & (1 << i_bit);
            if (!in_use) {
                int j = i;
                for (; j < ((int)sb->s_blocks_count) && consecutive_blk_count < blk_count; j++) {
                    int j_byte = j / 8;
                    int j_bit = 7 - j % 8;
                    char in_use = bm[j_byte] & (1 << j_bit);
                    if (!in_use) {
                        consecutive_blk_count += 1;
                    } else { break; }
                }
                if (consecutive_blk_count == blk_count) { return sb->s_data_blocks_addr + i ;}
                i = j;
            }
        }
    }
    return INT_MAX;
}

/**
 * <Peter>
 * return the address of the extent
 * 
 * @param  inode: the inode holding the extent
 * @param  id: the address of the extent in the extent table
 * @return the address of the extent in the file system (in byte unit)
 */
int search_extent(a1fs_inode *inode, int id) {
	return inode->extent_table_addr * A1FS_BLOCK_SIZE + id * sizeof(a1fs_extent);
}

/**
 * <Peter>
 * return the address of the dentry in the file system
 *
 * @param extent: the extent of the directory inode
 * @param id: the address of the dentry in the data block of the directory
 * @return the address of the dentry on the file system (in byte unit)
 */
int search_next_dentry(a1fs_extent* extent, int id) {
	return extent->start * A1FS_BLOCK_SIZE + id * sizeof(a1fs_dentry);
}

/**
 * 1 <Peter> [5]
 * break the path to an array of names of directories in the path and
 * return a struct with an array of names in the path and the number of directories (the final one may be a file)
 * 
 * @param path: the input absolute path to the file/directory
 * @return a struct with an array of names in the path and the number of directories (the final one may be a file)
 */
path_names* break_path_to_names(const char *path) {
    // copy the path because a constant strong cannot be used in strtok
    char temp_path[strlen(path) + 1];
    temp_path[strlen(path)] = '\0';
    strncpy(temp_path, path, strlen(path));

    // allocate a struct to store broken path and add the root directory at first
    path_names *names;
    names = malloc(sizeof(path_names));
    if (!names) { return NULL; }
    names->names[0] = malloc(sizeof(char) * 2);
    if (!(names->names[0])) { return NULL; }
    names->names[0][1] = '\0';
    names->names[0][0] = '/';
   
    // put the names in the path in the struct
    char* name;
    int name_count = 1;
    name = strtok(temp_path, "/");
    while (name) {
        names->names[name_count] = malloc(sizeof(char) * (strlen(name) + 1));
        if (!(names->names[name_count])) { return NULL; }
        (names->names[name_count])[strlen(name)] = '\0';
        strncpy(names->names[name_count], name, strlen(name));

        name = strtok(NULL, "/");
        name_count++;
    }
    names->path_count = name_count;
    return names;
}

/**
 * 2 <Peter> [5]
 * 
 * free the struct storing names in the path
 * 
 * @param path: path to the file/directory
 */
void free_path_names(path_names *names) {
    for (int i = 0; i < names->path_count; i++) { free(names->names[i]); };
    free(names);
}


 /**
 * 3 <Tony> [5]
 *
 * get the first empty extent places(free extent)
 *
 * @param 	ino  inode.
 * @param 	image disk image.
 * @return  extent id on success; -1 on not found.
 */
int get_next_free_extent_id(a1fs_inode *ino, unsigned char *image) {
	a1fs_extent *ex_table = (a1fs_extent *)(image + ino->extent_table_addr * A1FS_BLOCK_SIZE);
	for (int i=0; i < (int)(A1FS_BLOCK_SIZE/sizeof(a1fs_extent)); i++) {
		if(ex_table[i].count == 0) {
			return i;
		}
	}
	return -1;
}


 /**
 * 4 <Tony> [5]
 *
 * find the inode associated with the path destination
 *
 * @param 	ino  inode.
 * @param 	image disk image.
 * @return  inode id on success; -1 on not found; -2 on incorrect format.
 */
unsigned int get_inode_by_fname(path_names *p_n, unsigned char *image) {
	a1fs_superblock *sb = (a1fs_superblock *)image;
	a1fs_inode *root_i = (a1fs_inode *)(image + (sb->s_inode_table_addr) * A1FS_BLOCK_SIZE);
	a1fs_inode *cur_i = root_i;
	int find = 0;
	unsigned int cur_i_addr = 0;
	// for the root inode as entry
	if (strcmp("/", (p_n->names)[0]) == 0) {
		find = 1;
		cur_i_addr = 0;
	}
	if (find) {
		// Iterate through the path
		for (int i=1; i < p_n->path_count; i++) {
			find = 0;
			unsigned int cur_ex_blk_addr = cur_i->extent_table_addr;
			a1fs_extent *cur_ex_table = (a1fs_extent *)(image + cur_ex_blk_addr * A1FS_BLOCK_SIZE);
			short ex_count = cur_i->extent_count;
			if (S_ISDIR(cur_i->mode | S_IFDIR)) {
				// If this is a dir, go through all the extent to get dentries
				for (int j=0; j < ex_count; j++) {
					a1fs_blk_t ex_start = cur_ex_table[j].start;
					a1fs_blk_t length = cur_ex_table[j].count;
					a1fs_dentry *dentrys = (a1fs_dentry *)(image + ex_start * A1FS_BLOCK_SIZE);
					// Go though all the dentries to compare the path name and dentry name
					for (int k=0; k < (int)(length * (A1FS_BLOCK_SIZE/sizeof(a1fs_dentry))); k++) {
						if (strcmp(dentrys[k].name, (p_n->names)[i]) == 0) {
							cur_i_addr = dentrys[k].ino;
							cur_i = root_i + cur_i_addr;
							if (!S_ISDIR(cur_i->mode | S_IFDIR)) {
								if (i == (p_n->path_count -1)) {
									// if the name matches
									// and it is the end of path
									return cur_i_addr;
								}
								else {
									// if it is not the end of path
									// but it is not a dir
									return -2;
								}
							}
							find = 1;
						}
					}
				}
			}
			if (!find) {
				return -1;
			}
		}
		return cur_i_addr;
	}
	return -1;
}

/**
 * <Peter>
 * return the inode by the index given
 * 
 * @param disk: the disk of the file system
 * @param id: the address of the inode
 * @param sb: the superblock of the file system
 * @return the inode at the given position
 */
a1fs_inode *find_inode_by_index(unsigned char *disk, int id, a1fs_superblock *sb) {
    return (a1fs_inode *)(disk + sb->s_inode_table_addr * A1FS_BLOCK_SIZE + id * sizeof(a1fs_inode));
}

 /**
 * 4 <Tony> [5]
 *
 * update the inode bitmap by given bit(1 or 0) and the index
 *
 * @param image disk image.
 * @param addr 	index of the bitmap
 * @param bit	1 or 0
 */
void update_inode_bitmap(unsigned char *image, int addr, int bit) {
	a1fs_superblock *sb = (a1fs_superblock *)image;
	unsigned char *bm = image + (sb->s_inode_bitmap_addr) * A1FS_BLOCK_SIZE;
	update_bitmap(bm, addr, bit);
}


 /**
 * 4 <Tony> [5]
 *
 * update the data bitmap by given bit(1 or 0) and the index
 *
 * @param image disk image.
 * @param addr 	index of the bitmap
 * @param bit	1 or 0
 */
void update_data_bitmap(unsigned char *image, int addr, int bit) {
	a1fs_superblock *sb = (a1fs_superblock *)image;
	unsigned char *bm = image + (sb->s_data_bitmap_addr) * A1FS_BLOCK_SIZE;
	update_bitmap(bm, addr - sb->s_data_blocks_addr, bit);
}

 /**
 * 4 <Tony> [5]
 *
 * update the bitmap by given bit(1 or 0) and the index
 *
 * @param image disk image.
 * @param addr 	index of the bitmap
 * @param bit	1 or 0
 */
void update_bitmap(unsigned char *bm, int addr, int bit) {
	int n_byte = addr / 8;
	int n_bit = addr % 8;
	int cur_bit = bm[n_byte] & (1 << (7 - n_bit));
	// flip the bit if the existing bit is different from the desire bit
	if ((!bit && cur_bit) || (bit && !cur_bit)) {
		bm[n_byte] ^= 1 << (7 - n_bit);
	}
}

 /**
 * compare two extents by start position and length
 *
 * @param  self 	one extent to compare.
 * @param  other 	other extent to compare.
 * @return 1 on success; 0 on fail
 */
int cmp_extent(a1fs_extent *self, a1fs_extent *other) {
    return (self->start == other->start && self->count == other->count);
}

/**
 * 6 <Peter> [5]
 * find the extent with the given information
 * 
 * @param   inode: the inode of a file/directory
 * @param   extent: the extent needs to be found (with the same info)
 * @param   disk: the disk of the file system
 */
int find_extent(a1fs_inode *inode, a1fs_extent *extent, unsigned char *disk) {
    // the address of the extent
    int id, return_id = 0;
    bool flag = true;
    for (; id < inode->extent_count && flag; id++) {
        a1fs_extent *other = (a1fs_extent *)(disk + search_extent(inode, id));
        if (cmp_extent(extent, other)) {
            flag = false;
            return_id = id;
        }
    }
    if (flag) { return -1; }
    return return_id;
}

/**
 * 5 <Peter> [6]
 * delete an extent at given index to avoid any segmentation in the extent table
 * 
 * @param inode: inode of a file or directory
 * @param id: the address of the extent to be deleted
 * @param disk: the disk of the file system
 */
void del_extent(a1fs_inode *inode, int id, unsigned char* disk) {
    assert(id >= 0 && inode->extent_count > id);
    
    clock_gettime(CLOCK_REALTIME, &(inode->dtime));
    clock_gettime(CLOCK_REALTIME, &(inode->mtime));

    int i = id;
    // move each extent forward, starting from the extent deleted
    for (; i < inode->extent_count - 1; i++) {
        a1fs_extent *curr = (a1fs_extent *)(disk + search_extent(inode, i));
        a1fs_extent *next = (a1fs_extent *)(disk + search_extent(inode, i + 1));
        *curr = *next;
    }
    // decrease the number of extents of an inode by 1
    inode->extent_count--;
    // delete the last extent
    memset(disk + search_extent(inode, i), 0, sizeof(a1fs_extent));
}

/**
 * <Peter>
 * delete all contents in the file/directory represented by the input inode
 * 
 * @param sb: the superblock of the file system
 * @param inode: the inode of the file/directory to be deleted
 * @param disk: the disk of the file system
 */
void del_contents_inode(a1fs_superblock *sb, a1fs_inode *inode, unsigned char* disk) {
    a1fs_extent *extent_table = (a1fs_extent *)(disk + A1FS_BLOCK_SIZE * inode->extent_table_addr);
    for (int i = 0; i < inode->extent_count; i++) {
        a1fs_extent extent = extent_table[i];
        sb->s_free_blocks_count += extent.count;
        for (unsigned int i = 0; i < extent.count; i++) {
            update_data_bitmap(disk, extent.start + i, 0);
        }
        memset(disk + A1FS_BLOCK_SIZE * extent.start, 0, A1FS_BLOCK_SIZE * extent.count);
    }
    sb->s_free_blocks_count++;
    update_data_bitmap(disk, inode->extent_table_addr, 0);
    memset(extent_table, 0, A1FS_BLOCK_SIZE);
}


/**
 * 9 <Peter> [5]
 * (serve for terminal command ls) fill the names of all dentries of a directory into the given buf
 * work without the assumption that all dentries are consecutive
 * 
 * @param inode: the inode of the directory
 * @param buf: the buf to be found
 * @param filler: the function used in fuse.h to fill the buf
 * @param disk: the disk of the file system
 * @return -1 for error and 0 for success
 */
int fill_dentry_buf(a1fs_inode *inode, void *buf, fuse_fill_dir_t filler, unsigned char *disk) {
    // only work if the given inode represents a directory
    if (S_ISDIR(inode->mode | S_IFDIR)) {
        a1fs_extent *extent;
        a1fs_dentry *dentry;
        int blk_dentry_count = 0;
        // iterate through each extent of the directory
        for (int id = 0; id < inode->extent_count; id++) {
            extent = (a1fs_extent *)(disk + search_extent(inode, id));
            unsigned int blk_id = 0;
            // search each block in an extent
            for (;  blk_id < ((extent->count * A1FS_BLOCK_SIZE) / sizeof(a1fs_dentry))
                    && blk_dentry_count < num_dentry(inode); blk_id++) {
                dentry = (a1fs_dentry *)(disk + search_next_dentry(extent, blk_id));
                // if the dentry is not empty, fill the name of a denry into buf
                if (!dentry_isempty(dentry)) {
                    if (filler(buf, dentry->name, NULL, 0) != 0) {
                        return -1;
                    }
                    blk_dentry_count++;
                }
            }
        }
    }
    return 0;
}

/**
 * 9 <Peter> [5]
 * 
 * find next free dentry for a directory (each denry is not necessarily consecutive)
 */
a1fs_dentry* find_free_dentry(a1fs_superblock *sb, a1fs_inode *inode, unsigned char *disk) {
    if (S_ISDIR(inode->mode | S_IFDIR)) {
        a1fs_extent *extent;
        a1fs_dentry *dentry;
        int blk_dentry_count = 0;
        // search for each extent
        for (int id = 0; id < inode->extent_count; id++) {
            extent = (a1fs_extent *)(disk + search_extent(inode, id));
            unsigned int blk_id = 0;
            // search for each data block
            for (;  blk_id < ((extent->count * A1FS_BLOCK_SIZE) / sizeof(a1fs_dentry)); blk_id++) {
                dentry = (a1fs_dentry *)(disk + search_next_dentry(extent, blk_id));
                // fprintf(stderr, "%s\n", dentry->name);
                if (dentry_isempty(dentry)) {
                    return dentry;
                }
		        blk_dentry_count++;
            }
        }
        // add new extent to the directory if there is no enough space for the dentry
        if (inode->extent_count >= 512) {
            return NULL;
        }
        extent = get_free_extent_place(disk, inode);
        int blk_count = 2;
        extent->start = search_next_free_dlk(disk, sb, blk_count);
        if ((int) extent->start == -1) {
            return NULL;
        }
        if (sb->s_next_free_data_block_addr + blk_count < sb->s_data_blocks_addr + sb->s_blocks_count) {
            sb->s_next_free_data_block_addr += 2;
        }
        extent->count = 2;
        memcpy(disk + A1FS_BLOCK_SIZE * inode->extent_table_addr + sizeof(dentry) * num_dentry(inode), &extent, sizeof(a1fs_extent));
        sb->s_free_blocks_count -= 2;
        inode->extent_count++;

        update_data_bitmap(disk, extent->start, 1);
        update_data_bitmap(disk, extent->start + 1, 1);

        dentry = (a1fs_dentry *)(disk + search_next_dentry(extent, 0));
        return dentry;
    }
    return NULL;
}


/**
 * 9 <Peter> [5]
 * find the dentry with the input file/directory name
 * 
 * @param   inode: the inode may hold the dentry
 * @param   name:  the name of the dentry
 * @param   disk:  the disk of the file system
 * @return  the dentry with name
 */
a1fs_dentry *find_expected_dentry(a1fs_inode *inode, char *name, unsigned char *disk) {
    if (S_ISDIR(inode->mode | S_IFDIR)) {
        a1fs_extent *extent;
        a1fs_dentry *dentry;
        int blk_dentry_count = 0;
        // search for each extent the inode holds
        for (int id = 0; id < inode->extent_count; id++) {
            extent = (a1fs_extent *)(disk + search_extent(inode, id));
            unsigned int blk_id = 0;
            // search for each block in the extent
            for (;  blk_id < ((extent->count * A1FS_BLOCK_SIZE) / sizeof(a1fs_dentry))
                    && blk_dentry_count < num_dentry(inode); blk_id++) {
                dentry = (a1fs_dentry *)(disk + search_next_dentry(extent, blk_id));
                // return the pointer of the dentry with the name
                if (strcmp(dentry->name, name) == 0) {
                    return dentry;
                }
            }
            blk_dentry_count += blk_id;
        }
    }
    return NULL;
}

 /**
 * get the next free extent instance
 *
 * @param  image 	disk image.
 * @param  inode 	the inode to search.
 * @return address of the extent place.
 */
a1fs_extent *get_free_extent_place(unsigned char *image, a1fs_inode *inode) {
	unsigned int ex_table_addr = inode->extent_table_addr;
	a1fs_extent *free_place = (a1fs_extent *)(image + ex_table_addr * A1FS_BLOCK_SIZE);
	return free_place+inode->extent_count;
}


 /**
 * ceilling function
 *
 * @param  n 	number.
 * @return ceriling of n.
 */
int cei(double n) {
	if (n == (int)n) {
		return n;
	}
	return (int)n + 1;
}

// Check whether the disk can store s_in_byte amount of data in free data blocks
int check_db_space(unsigned char *image, unsigned int s_in_byte) {
	a1fs_superblock *sb = (a1fs_superblock *)(image);
	return cei((double)(s_in_byte)/A1FS_BLOCK_SIZE) <= (int)(sb->s_free_blocks_count);
}

/**
 * <Peter>
 * (serve for truncate) shrink the space occupied by a file by the given number of blocks
 * 
 * @param  disk: the disk for the file system
 * @param  inode: the inode for the file
 * @param  blocks_del_count: the number of blocks deleted
 */
void shrink(unsigned char* disk, a1fs_inode* inode, int blocks_del_count) {
    unsigned int d_count = blocks_del_count;
    a1fs_extent* extent_table = (a1fs_extent *)(disk + A1FS_BLOCK_SIZE * inode->extent_table_addr);
    for (int i = inode->extent_count - 1 && d_count > 0; i >= 0; i--) {
        a1fs_extent shrinked_extent = extent_table[i];
        // if there are blocks needed to be deleted than the extent has
        // delete the extent
        if (shrinked_extent.count <= d_count) {
            d_count -= shrinked_extent.count;
            for (unsigned int j = 0; j < shrinked_extent.count; j++) {
                update_data_bitmap(disk, j + shrinked_extent.start, 0);
            }
            memset(disk + A1FS_BLOCK_SIZE * shrinked_extent.start, 0, A1FS_BLOCK_SIZE * shrinked_extent.count);
            memset(extent_table + i * sizeof(a1fs_extent), 0, sizeof(a1fs_extent));
            inode->extent_count--;
        // delete some blocks of the extent if the extent has more blocks
        } else {
            int del_start = shrinked_extent.start + (shrinked_extent.count - d_count);
            for (unsigned int j = 0; j < d_count; j++) {
                update_data_bitmap(disk, j + del_start, 0);
            }
            memset(disk + A1FS_BLOCK_SIZE * del_start, 0, d_count * sizeof(A1FS_BLOCK_SIZE));
            shrinked_extent.count -= d_count;
            d_count = 0;
        }
    }
}

/**
 * <Peter>
 * Read the number of bytes from the file input starting from the input offset
 * (invariant: the order of the extent of an inode obey the order of bytes to be read)
 * 
 * @param  disk: the disk of the file system
 * @param  inode: the inode of the file
 * @param  size: the max size of bytes to be read
 * @param  offset: the starting byte to be read
 * @param  buf: the buf holding the bytes read
 * @return the number of bytes actually read
 */
int read_all(unsigned char *disk, a1fs_inode *inode, size_t size, off_t offset, char *buf) {
    int prev_block_count = 0;
    // the index of the byte in the buf
    int buf_offset = 0;
    // the index of the block to start reading
    int block_offset = offset / A1FS_BLOCK_SIZE;
    // the number of bytes have been read
    int read_size = 0;
    // the table of the extent of an inode
    a1fs_extent *extent_table = (a1fs_extent *)(disk + A1FS_BLOCK_SIZE * inode->extent_table_addr);

    for (int i = 0; i < inode->extent_count && read_size < (int) size && offset + read_size < (int) inode->size; i++) {
        a1fs_extent extent = extent_table[i];
        // before the block needs to be read
        if (block_offset > prev_block_count) {
            // the first block to be read in the extent
            if (block_offset < prev_block_count + ((int) extent.count)) {
                int byte_offset = offset % A1FS_BLOCK_SIZE;
                // read a block
                for (int j = byte_offset; j < A1FS_BLOCK_SIZE && read_size < ((int) size); j++) {
                    buf[buf_offset] = *(disk + A1FS_BLOCK_SIZE * extent.start + j);
                    read_size++;
                    buf_offset++;
                }
            }
        // if the first byte needs to be read has been read
        } else {
            // read all bytes in an extent
            for (int j = 0; j < ((int) extent.count) * A1FS_BLOCK_SIZE && read_size < ((int) size) &&
                    offset + read_size < ((int) inode->size); j++) {
                buf[buf_offset] = *(disk + A1FS_BLOCK_SIZE * extent.start + j);
                read_size++;
                buf_offset++;
            }
        }
        prev_block_count += extent.count;
    }
    return read_size;
}
/**
 * <Tony>
 * Find the next nonempty extent follow by a current index
 * 
 * @param   disk 	disk image.
 * @param   ino 	the inode to search.
 * @param   cur		current index 
 * @return  1 on success; -1 on fail
 */
int next_nonempty_extent(unsigned char* disk, a1fs_inode *ino, int cur) {
	a1fs_extent *ex_table = (a1fs_extent *)(disk + ino->extent_table_addr * A1FS_BLOCK_SIZE);
	for (int i = cur + 1; i < 512; i++) {
		if (ex_table[i].start != 0) {
			return i;
		}
	}
	return -1;
}
