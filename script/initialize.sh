src_dir=$(cd "$(dirname "$0")"; pwd)/source-code
echo "src_dir: $src_dir"
echo "remove: $src_dir"
rm -rf $src_dir
git clone https://github.com/zhiyun168/recommend-middleman.git $src_dir
