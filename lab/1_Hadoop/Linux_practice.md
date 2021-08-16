#### cd：切换目录

```bash
# 切换到指定目录
cd /usr/local
# 切换到当前目录的上一级目录
cd ./..
# 切换到当前用户的主文件夹
cd ~
```

#### ls：查看文件和目录

```bash
# 以列表方式展示指定目录下的所有文件和目录
ls -al /usr
```

#### mkdir：新建目录

```bash
# 进入/tmp目录，创建名为“a”的目录
cd /tmp
mkdir a
# 查看/tmp目录下有哪些目录
ls -p | grep /$ # -p会使目录文件名末尾带上“/”

# 进入/tmp目录，创建目录“a1/a2/a3/a4”
cd /tmp
mkdir -p a1/a2/a3/a4 # -p表示自动创建父目录
```

#### rmdir：移除空目录

```bash
rmdir /tmp/a
rmdir /tmp/a1/a2/a3/a4
ls -p | grep /$ # a1/a2/a3依然存在
```

#### cp：复制文件或目录

```bash
# 复制.bashrc并重命名
sudo cp ~/.bashrc /usr/bashrc1
# 复制目录
mkdir /tmp/test
sudo cp -r /tmp/test /usr/test # -r表示递归
```

#### mv：移动文件或目录

```bash
sudo mv /usr/bashrc1 /usr/test/bashrc1
# 重命名
sudo mv /usr/test /usr/test2
```

#### rm：删除文件或目录

```bash
# 删除文件
sudo rm /usr/test2/bashrc1
# 删除目录
sudo rm -r /usr/test2
```

#### cat：查看文件内容

```bash
cat ~/.bashrc
```

#### tac：反向查看文件内容

```bash
# 常用于查看日志文件
tac ~/.bashrc
```

#### more：翻页查看文件

```bash
more ~/.bashrc
# 按空格键下一页，按b上一页（back）
```

#### head：输出文本文件的前几行

```bash
# 查看文件内容前20行
head -n20 ~/.bashrc
# 后面50行不显示
head -n-50 ~/.bashrc
```

#### tail：输出文本文件的后几行

```bash
# 查看文件内容后20行
tail -n20 ~/.bashrc
# 前面50行不显示
tail -n-50 ~/.bashrc
```

#### touch：修改文件时间或创建新文件

```bash
# 在/tmp目录下创建空文件hello
touch /tmp/hello
# 查看文件时间
ls -l /tmp | grep hellp
# 修改文件时间
touch -m -d "2021-08-11 11:29" /tmp/hello
```

#### chown：修改文件所有者权限

```bash
# 将hello文件所有者改为root
sudo chown root /tmp/hello
```

#### find：查找文件

```bash
# 找出主目录下名为.bashrc的文件
find ~ -name ".bashrc"
```

#### tar：压缩和解压

```bash
# 在根目录下新建文件夹test，原地打包成test.tar.gz
sudo mkdir /test
sudo tar -zcf /test.tar.gz /test 
# 把该压缩包解压至/tmp
sudo tar -zxf /test.tar.gz -C /tmp
```

#### grep：查找字符串

```bash
# 在~/.bashrc文件中查找字符串"examples"
cat ~/.bashrc | grep examples
```

#### 配置环境变量

```bash
# 在 ~/.bashrc 中配置 Java 环境变量
sudo vim ~/.bashrc
...
export JAVA_HOME=... # JDK所在路径
export JRE_HOME=$JAVA_HOME/jre
export CLASSPATH=.:$JAVA_HOME/lib:$JRE_HOME/lib
export PATH=$PATH:$JAVA_HOME/bin

# 让.bashrc生效
source ~/.bashrc

# 查看 JAVA_HOME 变量的值
echo $JAVA_HOME
```

