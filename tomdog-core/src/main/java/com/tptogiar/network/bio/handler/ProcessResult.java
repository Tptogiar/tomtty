package com.tptogiar.network.bio.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SocketChannel;

/**
 * @author Tptogiar
 * @description
 * @date 2022/6/4 - 19:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessResult {

    private byte[] responseHeaderBytes;

    private byte[] responseBytes;

    protected boolean isFileTransfer = false;

    protected boolean isKeepAlive = false;

    private FileChannel srcFileChannel;

    public ProcessResult(boolean isFileTransfer) {
        this.isFileTransfer = isFileTransfer;
    }

    public ProcessResult(boolean isFileTransfer, FileChannel srcFileChannel) {
        this.isFileTransfer = isFileTransfer;
        this.srcFileChannel = srcFileChannel;
    }
}