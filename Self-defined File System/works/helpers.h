// Using 2.9.x FUSE API
#define FUSE_USE_VERSION 29
#include <fuse.h>
#include <limits.h>

#include "a1fs.h"

int num_dentry(a1fs_inode *inode);

int search_next_free_inode(unsigned char* disk, a1fs_superblock *sb);

int search_next_free_dlk(unsigned char* disk, a1fs_superblock *sb, unsigned int blk_count);

int search_extent(a1fs_inode *inode, int id);

bool dentry_isempty(a1fs_dentry *dentry);

int search_next_dentry(a1fs_extent* extent, int id);

typedef struct path_names_t {
    char* names[A1FS_PATH_MAX];
    int path_count;
} path_names;

/* Peter */
path_names* break_path_to_names(const char *path);

void free_path_names(path_names *names);

/* 2 <Peter> [5] */
unsigned int get_inode_by_fname(path_names *p_n, unsigned char *image);

/* Peter */
a1fs_inode *find_inode_by_index(unsigned char *disk, int id, a1fs_superblock *sb);

/* 3 <Tony> [5] */
int get_next_free_extent_id(a1fs_inode *ino, unsigned char *image);

a1fs_inode *get_inode(char **names, unsigned char *image, char mode, int *err);

/* 4 <Tony> [5] */
void update_inode_bitmap(unsigned char *image, int addr, int bit);

void update_data_bitmap(unsigned char *image, int addr, int bit);

void update_bitmap(unsigned char *bm, int addr, int bit);

int cmp_extent(a1fs_extent *self, a1fs_extent *other);

/* 6 <Peter> [5]*/
int find_extent(a1fs_inode *inode, a1fs_extent *extent, unsigned char *disk);

/* 5 <Peter> [5]*/
void del_extent(a1fs_inode *inode, int index, unsigned char *disk);

/* Peter */
void del_contents_inode(a1fs_superblock *sb, a1fs_inode *inode, unsigned char* disk);

/* 7 <Tony> [3] */
void blocks_transfer(unsigned char *image, a1fs_blk_t cpy_to, a1fs_blk_t cpy_from, int length);

/* 8 <Tony> [3] */
void arrange_extents(unsigned char *image, a1fs_inode *inode, a1fs_extent *extent);

/* Peter */
int fill_dentry_buf(a1fs_inode *inode, void *buf, fuse_fill_dir_t filler, unsigned char *disk);

a1fs_dentry* find_free_dentry(a1fs_superblock *sb, a1fs_inode *inode, unsigned char *disk);

/* 9 <Peter> [5] */
a1fs_dentry *find_expected_dentry(a1fs_inode *inode, char *name, unsigned char *disk);

/* 10 <Tony> [5] */
a1fs_extent *get_free_extent_place(unsigned char *image, a1fs_inode *inode);

/* 11 <Tony> [5]*/
unsigned char *get_file_eof(unsigned char *image, a1fs_inode *inode, unsigned long int *remaining);

int cei(double n);

/* 12 <Tony> [5]*/
int check_db_space(unsigned char *image, unsigned int s_in_byte);

/* Peter */
//void shrink(unsigned char* disk, a1fs_inode* inode, a1fs_superblock* sb, int blocks_del_count);
void shrink(unsigned char* disk, a1fs_inode* inode, int blocks_del_count);

int read_all(unsigned char *disk, a1fs_inode *inode, size_t size, off_t offset, char *buf);

int next_nonempty_extent(unsigned char* disk, a1fs_inode *ino, int cur);
