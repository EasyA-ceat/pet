const fs = require('fs');
const iconv = require('iconv-lite');

// 读取文件的原始字节内容
const rawBuffer = fs.readFileSync('任务进度报告.md');

// 尝试使用不同的编码来解码
const encodings = ['utf8', 'utf16', 'gbk', 'gb2312', 'big5'];

for (let encoding of encodings) {
  try {
    const decoded = iconv.decode(rawBuffer, encoding);
    console.log(`成功使用 ${encoding} 编码解码文件`);
    console.log('文件内容预览（前 500 字节）：');
    console.log(decoded.substring(0, 500));
    
    // 保存为 UTF-8 编码
    fs.writeFileSync('任务进度报告.md', decoded, 'utf8');
    console.log('文件已成功转换为 UTF-8 编码');
    break;
  } catch (e) {
    console.log(`使用 ${encoding} 解码失败: ${e.message}`);
  }
}
