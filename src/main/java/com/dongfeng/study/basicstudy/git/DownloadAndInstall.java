package com.dongfeng.study.basicstudy.git;

/**
 * Git下载和安装
 *
 * @author eastFeng
 * @date 2022-11-23 9:28
 */
public class DownloadAndInstall {


    /*
     * 教程：https://blog.csdn.net/mukes/article/details/115693833
     */

    /*
     * 一、下载：
     * 在Git官网下载：https://git-scm.com/
     * 官网有不同系统不同平台的安装包和源代码。
     * 例如我的电脑是64位的，并且是Windows系统，下载官网提供的Git-2.38.1-64-bit.exe可执行安装包。
     * 下载好之后，就可以鼠标左键双击该文件进行安装了。
     *
     * 二、安装：
     * 根据提示一步步安装。
     * 1. 双击后的第一个界面主要是这个界面主要展示了GPL第2版协议1的内容，点击 [next] 到第二步。
     * 2. 第二个界面是选择安装目录。
     *    可点击 “Browse…” 更换目录，也可直接在方框里面改，
     *    一般安装在D盘里，例如我安装到了D:\WorkSoftware\Git目录。点击 [next] 到第三步。
     * 3. 第三个界面是选择安装组件。
     *    比如必选的Git Bash（Git的命令行操作工具）和Git GUI（Git的图形化操作工具）等等。
     *    图中这些英文都比较简单，大家根据自己的需要选择勾选。点击 [next] 到第四步。
     *    Additional icons：附加图标
     *       On the Desktop：桌面快捷方式   // 可以勾选
     *    Windows Explorer integration：Windows 资源管理器集成鼠标右键菜单
     *       Git Bash Here  // 勾选
     *       Git GUI Here   // 勾选
     *    Git LFS (Large File Support)：大文件支持   // 勾选
     *    Associate .git* configuration files with the default text editor：将 .git* 配置文件与默认文本编辑器相关联  // 勾选
     *    Associate .sh files to be run with Bash：关联 .sh 格式文件  // 勾选
     *    Check daily for Git for Windows updates：每天检查 Git 是否有 Windows 更新  // 看自己心情
     *    (NEW!) Add a Git Bash Profile to Windows Terminal：将 Git Bash 的配置文件添加到 Windows 终端  // 勾选的话，需要下载 Windows Terminal 配合 Git Bash使用
     *    (NEW!) Scalar(Git add-on to manager large-scale repositories)：Scalar（管理大型存储库的Git插件） // 勾选
     * 4. 选择开始菜单文件夹。
     *    方框内 Git 可改为其他名字，也可点击 “Browse...” 选择其他文件夹
     *    或者给"Don't create a Start Menu folder" 打勾不要文件夹，
     *    建议不要修改，直接点击 [next] 到第五步。
     * 5. 选择Git默认编辑器（choosing the default editor used by Git）。
     *    Git 安装程序里面内置了 10 种编辑器供你挑选，比如 Atom、Notepad、Notepad++、Sublime Text、Visual Studio Code、Vim 等等，
     *    默认的是 Vim ，选择 Vim 后可以直接进行到下一步，但是 Vim 是纯命令行，操作有点难度，需要学习。
     *    如果选其他编辑器，则还需要去其官网安装后才能进行下一步。
     *    建议不要修改，直接点击 [next] 到第六步。
     * 6. 决定初始化新项目(仓库)的主干名字（Adjusting the name of the initial branch in new repositories）。
     *    第一种（Let Git decide）是让 Git 自己选择，名字是 master ，但是未来也有可能会改为其他名字；
     *    第二种（Override the default branch name for new repositories）是我们自行决定，默认是 main，当然，你也可以改为其他的名字。
     *    一般默认第一种，点击 [next] 到第七步。
     *    注： 第二个选项下面有个 NEW！ ，说很多团队已经重命名他们的默认主干名为 main .
     *    这是因为2020 年非裔男子乔治·弗洛伊德因白人警察暴力执法惨死而掀起的 Black Lives Matter(黑人的命也是命)运动，
     *    很多人认为 master 不尊重黑人，呼吁改为 main。
     * 7. 调整你的 path 环境变量（Adjusting your PATH environment）。
     *    有三种可选操作：
     *    Use Git from Git Bash only
     *    This is the most cautious choice as your PATH will not be modified at all. You w only be able to use the Git command line tools from Git Bash.
     *    仅从 Git Bash 使用 Git。
     *    这是最谨慎的选择，因为您的 PATH 根本不会被修改。您将只能使用 Git Bash 中的 Git 命令行工具。
     *
     *    Git from the command line and also from 3rd-party software
     *    (Recommended) This option adds only some minimal Git wrappers to your PATH to avoid cluttering your environment with optional Unix tools.
     *    You will be able to use Git from Git Bash, the Command Prompt and the Windov PowerShell as well as any third-party software looking for Git in PATH.
     *    从命令行以及第三方软件进行 Git
     *   （推荐）此选项仅将一些最小的 Git 包装器添加到PATH中，以避免使用可选的 Unix 工具使环境混乱。
     *    您将能够使用 Git Bash 中的 Git，命令提示符和 Windov PowerShell 以及在 PATH 中寻找 Git 的任何第三方软件。
     *
     *    Use Git and optional Unix tools from the Command Prompt
     *    Both Git and the optional Unix tools will be added to your PATH.
     *    Warning: This will override Windows tools like "find"and "sort". Only use this option if you understand the implications.
     *    使用命令提示符中的 Git 和可选的 Unix 工具
     *    Git 和可选的 Unix 工具都将添加到您的 PATH 中。
     *    警告：这将覆盖 Windows 工具，例如 "find" and "sort". 仅在了解其含义后使用此选项。
     *
     *    第一种是仅从 Git Bash 使用 Git。这个的意思就是你只能通过 Git 安装后的 Git Bash 来使用 Git ，
     *    其他的什么命令提示符啊等第三方软件都不行。
     *    第二种是从命令行以及第三方软件进行 Git。这个就是在第一种基础上进行第三方支持，
     *    你将能够从 Git Bash，命令提示符(cmd) 和 Windows PowerShell 以及可以从 Windows 系统环境变量中寻找 Git 的任何第三方软件中使用 Git。推荐使用这个。
     *    第三种是从命令提示符使用 Git 和可选的 Unix 工具。选择这种将覆盖 Windows 工具，
     *    如 “ find 和 sort ”。只有在了解其含义后才使用此选项。一句话，适合比较懂的人折腾。
     *
     *    直接选择第二种，点击 [next] 到第八步。
     *
     * 8. 选择 SSH 执行文件（choosing the SSH executable）。
     *    按默认来，点击 [next] 到第九步。
     * 9. 选择HTTPS后端传输（choosing HTTPS transport backend）。
     *    按默认来，点击 [next] 到第十步。
     * 10. 配置行尾符号转换。
     *     按默认来，点击 [next] 到第十一步。
     * 11. 配置终端模拟器以与 Git Bash 一起使用。
     *     建议选择第一种，MinTTY 3功能比 cmd 多，cmd 只不过 比 MinTTY 更适合处理 Windows 的一些接口问题，这个对 Git 用处不大，除此之外 Windows 的默认控制台窗口（cmd）有很多劣势，比如 cmd 具有非常有限的默认历史记录回滚堆栈和糟糕的字体编码等等。
     *     相比之下，MinTTY 具有可调整大小的窗口和其他有用的可配置选项，可以通过右键单击的工具栏来打开它们 git-bash 。
     *     点击 [next] 到第十二步。
     * 12. 选择默认的 “git pull” 行为。
     *     第一个是 merge
     *     第二个是 rebase
     *     第三个是 直接获取
     *
     *     第一种 git pull = git fetch + git merge
     *     第二种 git pull = git fetch + git rebase
     *     第三种 git pull = git fetch ？(这个没试过，纯属猜测
     *     一般默认选择第一项，git rebase 绝大部分程序员都用不好或者不懂，而且风险很大，但是很多会用的人也很推崇，但是用不好就是灾难。
     *     git pull 只是拉取远程分支并与本地分支合并，而 git fetch 只是拉取远程分支，怎么合并，选择 merge 还是 rebase ，可以再做选择。
     *
     *     按默认的第一种，点击 [next] 到第十三步。
     * 13. 选择一个凭证帮助程序。
     *     Git Credential Manager
     *     Use the cross-platform Git Credential Manager.
     *     See more information about the future of Git Credential Manager here.
     *     Git 凭证管理
     *     使用跨平台的 Git  凭证管理。
     *     在此处查看有关 Git 凭证管理未来的更多信息。
     *
     *     None
     *     Do not use a credential helper.
     *     不使用凭证助手。
     *
     *     一共两个选项：
     *     （1）Git 凭证管理
     *     （2）不使用凭证助手
     *     第一个选项是提供登录凭证帮助的，Git 有时需要用户的凭据才能执行操作；
     *     例如，可能需要输入用户名和密码才能通过 HTTP 访问远程存储库（GitHub，GItLab 等等）。
     *     选择第一种选项，点击 [next] 进到十四步。
     * 14. 配置额外的选项。
     *     默认第一种，点击 [next] 到第十五步。
     * 15. 配置实验性选项。
     *     这是实验性功能，可能会有一些小错误之类的，建议不用开启（全部都不勾选）。
     *     点击 [install] 进行安装。
     *
     * 三、Git工具
     * 1. Git Bash 是基于CMD的，在CMD的基础上增添一些新的命令与功能，平时主要用这个。
     * 2. Git CMD 不能说和 cmd 完全一样，只能说一模一样，功能少得可怜。
     * 3. Git FAQs 就是 Git Frequently Asked Questions（常问问题），
     *    访问地址：https://github.com/git-for-windows/git/wiki/FAQ
     * 4. Git GUI 就是 Git 的图形化界面
     * 5. Git Release Note 就是版本说明，增加了什么功能，修复了什么 bug 之类的。
     *
     */
}
