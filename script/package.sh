root_dir=$(cd "$(dirname "$0")"; pwd)
src_dir=$root_dir/source-code
echo "src_dir: $src_dir"
echo "cd ..."
cd $src_dir
git pull
echo "config files:"
ls config
echo "copy config files to resources"
cp -R config/* src/main/resources/
mvn package

