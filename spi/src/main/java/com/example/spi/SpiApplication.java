package com.example.spi;

import com.example.MainApplicaction;
import com.example.Search;

import java.util.List;

public class SpiApplication {



    public static class FileSearch implements Search {
        @Override
        public List<String> searchDoc(String keyword) {
            System.out.println("文件搜索 "+keyword);
            return null;
        }
    }
    public static class DatabaseSearch implements Search{
        @Override
        public List<String> searchDoc(String keyword) {
            System.out.println("数据搜索 "+keyword);
            return null;
        }
    }

    public static class RpcSearch implements Search{
        @Override
        public List<String> searchDoc(String keyword) {
            System.out.println("rpc搜索 "+keyword);
            return null;
        }
    }


    public static void main(String[] args) {
        // 运行spi的main函数
        MainApplicaction.main(new String[0]);
    }
}
