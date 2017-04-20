
<center><b>SCR(Shell Command Runner)</b>는 여러 서버에서 Shell을 실행하고, 결과를 로컬 디스크에 저장해주는 프로그램 입니다.</center>

특정 기간때(일,월,분기) 모든 서버에서 실행해야 하는 업무를 처리 할 때 사용 할 수 있습니다.

![SCR screen shot](https://lahuman.github.io/assets/project/scr/SCR.PNG)

<div markdown="0"><a href="https://github.com/lahuman/SCR" class="btn btn-warning">Source 바로가기</a></div>

## Download

### Template 파일
<div markdown="0"><a href="https://lahuman.github.io/assets/project/scr/SERVER_INFO.xml" class="btn btn-info">Shell Template</a></div>

### Windows 전용 실행 파일

<div markdown="0"><a href="https://lahuman.github.io/assets/project/scr/SCR_32.zip" class="btn btn-success">SCR 32bit</a></div>

<div markdown="0"><a href="https://lahuman.github.io/assets/project/scr/SCR_64.zip" class="btn btn-success">SCR 64bit</a></div>
      
### Linux 64bit 전용 실행 파일

<div markdown="0"><a href="https://lahuman.github.io/assets/project/scr/SCR_linux_64.jar" class="btn btn-success">SCR Linux 64bit</a></div>

#### Linux 실행 방법

``` bash
java -jar SCR_linux_64.jar
```


<div markdown="0"><a href="https://lahuman.github.io/assets/project/scr/SCR_CMD_linux_64.jar" class="btn btn-success">SCR COMMAND Linux 64bit</a></div>

##### COMMAND Line 실행 방법

``` bash
# 첫번째는 서버 정보, 두번째는 출력 파일 정보 기입 
java -jar SCR_linux_64.jar /data/SERVER_INFO.XML /data/output.txt
```

### 다른 OS의 경우 Source를 다운받아 실행 하셔야 합니다.

## Notice

* JRE 1.7 이상이 설치 되어 있어야 합니다.
* JRE_HOME 환경 변수가 설정 되어 있어야 합니다.
    * 다음이 path에 추가 되어야 함 
		* $JRE_HOME$\bin

## Function
* 여러 서버에 대한 처리
* 결과를 파일로 저장


## Update history

* 2013.07.23
    * 최초 배포



## License

SCR 는 open source 프로그램으로 MIT 라이선스를 따릅니다.

This SCR is free and open source software, distributed under the MIT License. So feel free to use this program on your project without linking back to me or including a disclaimer.
