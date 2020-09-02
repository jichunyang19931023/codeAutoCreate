package plugin.util;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;

import java.util.List;

public class FileUtil {

    public static void createFile(Project project, PsiDirectory workDir, String fileName, String packagePath, String content, List<String> warnInfos) {
        String[] packageArray = packagePath.split("\\.");
        PsiDirectory serviceDirectory = workDir;
        for (String packageStr : packageArray){
            if (!workDir.getVirtualFile().getPath().contains(packageStr)){
                // 如果目录不存在则创建
                if (serviceDirectory.findSubdirectory(packageStr) == null){
                    serviceDirectory = serviceDirectory.createSubdirectory(packageStr);
                }else{
                    serviceDirectory = serviceDirectory.findSubdirectory(packageStr);
                }
            }
        }
        // 查找需要生成的java文件
        PsiFile javaFile = serviceDirectory.findFile(fileName + ".java");
        // 如果文件已存在，则重命名为原文件名.old的格式，确保数据不丢失
        if (javaFile != null){
            String oldFileName = fileName + ".java.old";
            PsiFile renameFile = serviceDirectory.findFile(oldFileName);
            // 如果重命名文件已存在，则删除，使用新的重命名文件替换
            if (renameFile != null){
                renameFile.delete();
            }
            serviceDirectory.copyFileFrom(oldFileName, javaFile);
            javaFile.delete();
            // 将提示信息加到列表
            warnInfos.add(fileName + ".java已存在，重命名为" + oldFileName);
        }
        // 生成新文件
        PsiFile serviceFile = PsiFileFactory.getInstance(project).createFileFromText(fileName + ".java", JavaFileType.INSTANCE, content);
        serviceDirectory.add(serviceFile);
    }
}
