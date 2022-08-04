package com.example.projectclient.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Gmail {
    private String Subject;

    private String From;

    private String To;

    private int Details;

    private String file;

    private int countFile;

    private int FileSize;

    private String bodyEmail;

    private  Date  receivedDate;
}
