package com.dongfeng.study.basicstudy.git;

/**
 * Git：工作区和暂存区
 * @author eastFeng
 * @date 2022-11-23 13:41
 */
public class WorkspaceAndStageArea {
    /*
     * 教程：
     * 菜鸟教程 https://www.runoob.com/git/git-tutorial.html
     * 廖雪峰教程 https://www.liaoxuefeng.com/wiki/896043488029600
     */

    /*
     * 1. 工作区（workspace）
     *    比如我的learngit文件夹就是一个工作区。
     * 2. 版本库（Repository）
     *    工作区有一个隐藏目录.git，这个不算工作区，而是Git的版本库。
     *    Git的版本库里存了很多东西，其中最重要的就是称为stage（或者叫index）的暂存区，
     *    还有Git为我们自动创建的第一个分支master，以及指向master的一个指针叫HEAD。
     *
     *    把文件往Git版本库里添加的时候，是分两步执行的：
     *    第一步是用git add把文件添加进去，实际上就是把文件修改添加到暂存区；
     *    第二步是用git commit提交更改，实际上就是把暂存区的所有内容提交到当前分支。
     *    因为我们创建Git版本库时，Git自动为我们创建了唯一一个master分支，所以，现在，git commit就是往master分支上提交更改。
     *    可以简单理解为，需要提交的文件修改通通放到暂存区，然后，一次性提交暂存区的所有修改。
     *
     *    git add命令实际上就是把要提交的所有修改放到暂存区（Stage），
     *    然后，执行git commit就可以一次性把暂存区的所有修改提交到分支。
     *
     *    staging area：暂存区/缓存区
     *    local repository：版本库或本地仓库
     *    remote repository：远程仓库
     *
     *
     */
}
