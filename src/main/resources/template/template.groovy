<groovy>
result = '这里是编程的世界\n';
end = '时间会消逝';
for (item in items) {
    result += String.format("[%s](http://www.baidu.com)\n", item.name);
}
return result + end
</groovy>