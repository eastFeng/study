package com.dongfeng.study.basicstudy.git;

/**
 * Git常见的命令/操作
 *
 * @author eastFeng
 * @date 2022-11-24 19:00
 */
public class CommonOperations {

    /*
     * git config user.name                           # 查看用户名
     * git config user.email                          # 查看邮箱
     * git config --list                              # 查看配置列表
     * git config --global user.name "xxx"            # 配置用户名
     * git config --global user.email "xxx@xxx.com"   # 配置邮件
     *
     * git init                 # 初始化本地git仓库（创建新仓库），默认情况下Git就会为创建master分支
     * git clone                # clone远程仓库,例如: git clone https://gitee.com/getrebuild/rebuild.git
     * git status               # 查看当前版本状态（是否修改）
     * git add xyz.txt          # 添加xxx.txt文件至暂存区
     * git commit -m 'xxx'      # 把暂存区的所有内容提交到当前分支
     * git push origin master   # 将当前分支push到远程master分支
     * git merge origin/master  # 合并远程master分支至当前分支
     * git rm xxx.txt           # 删除暂存区中的xxx.txt文件
     * git log                  # 显示提交日志
     * git log -1               # 显示1行日志 -n为n行
     *
     * git show dfb02e6e4f2f7b573337763e5c0013802e392818   # 显示某个提交的详细内容
     *                                                     # show后面写的是commit id（版本号）
     * git show HEAD            # 显示HEAD提交日志
     * git tag                  # 显示已存在的tag
     *
     * git branch               # 显示本地分支, 当前分支前面会标一个*号
     * git branch -a            # 显示所有分支, 本地分支和远程分支
     * git branch -r            # 显示所有原创分支
     * git branch --merged      # 显示所有已合并到当前分支的分支
     * git branch --no-merged   # 显示所有未合并到当前分支的分支
     * git branch branchA       # 创建新的分支branchA
     * git checkout branchA     # 切换到分支branchA
     * git branch -d branchA    # 删除branchA这个分支
     *
     *
     */

}
