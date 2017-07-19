/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 **/

package com.dell.cpsd.common.integration.docker.compose.strategy;

import com.palantir.docker.compose.execution.Docker;
import com.palantir.docker.compose.execution.DockerCompose;
import com.palantir.docker.compose.execution.KillDownShutdownStrategy;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * <p>
 * This is the shutdown strategy class.  We need to delete the logs also, as some of them cause jenkins to fail the NextB scan.
 **/
public class DeleteLogsShutdownStrategy extends KillDownShutdownStrategy
{
    private final String logDirectoryToDelete;

    public DeleteLogsShutdownStrategy(String logDirectoryToDelete)
    {
        super();
        this.logDirectoryToDelete = logDirectoryToDelete;
    }

    @Override
    public void shutdown(DockerCompose dockerCompose, Docker docker) throws IOException, InterruptedException
    {
        super.shutdown(dockerCompose, docker);
        deleteLogs(logDirectoryToDelete);
    }

    private void deleteLogs(final String logDirectoryToDelete) throws IOException
    {
        Path directory = Paths.get(logDirectoryToDelete);
        Files.walkFileTree(directory, new SimpleFileVisitor<Path>()
        {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
            {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException
            {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
