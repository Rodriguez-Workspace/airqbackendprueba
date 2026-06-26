const fs = require('fs');
const path = require('path');

function walk(dir) {
    let results = [];
    const list = fs.readdirSync(dir);
    list.forEach(function(file) {
        file = path.join(dir, file);
        const stat = fs.statSync(file);
        if (stat && stat.isDirectory()) { 
            results = results.concat(walk(file));
        } else { 
            if (file.endsWith('Controller.java')) {
                results.push(file);
            }
        }
    });
    return results;
}

const controllers = walk('src/main/java/com/oxaira/airq');

for (const file of controllers) {
    const content = fs.readFileSync(file, 'utf8');
    const lines = content.split('\n');
    let basePath = '';
    console.log(`\n--- ${path.basename(file)} ---`);
    for (const line of lines) {
        const reqMatch = line.match(/@RequestMapping\("(.*?)"\)/);
        if (reqMatch) {
            basePath = reqMatch[1];
            console.log(`Base Path: ${basePath}`);
        }
        const methodMatch = line.match(/@(Get|Post|Put|Delete|Patch)Mapping(?:\("(.*?)"\))?/);
        if (methodMatch) {
            const method = methodMatch[1].toUpperCase();
            const endpoint = methodMatch[2] || '';
            console.log(`${method} ${basePath}${endpoint}`);
        }
    }
}
