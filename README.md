# Spring_Schedule_Management
필수 기능 구현과 테스트를 위한 브랜치입니다.</br>
각 엔티티에 대한 CRUD 기능과 JWT 인증 기능을 구현하고,</br>
구현한 기능들을 테스트합니다.

# 개발 로그 
커밋 기록에 대한 상세 설명</br>
</br>
===================================================</br>
</br>
feature : feature/core-functionality-implementation-and-testing 브랜치 생성</br>
</br>
필수 기능 구현과 테스트를 위한 브랜치 생성</br>
</br>
===================================================</br>
</br>
feature : JWT 설정 클래스 및 비밀번호 암호화 클래스 구현</br>
</br>
주요 변동사항 :</br>
jakarta.annotation-api 의존성을 추가적으로 주입하여</br>
@PostConstruct 어노테이션을 사용해</br>
애플리케이션 시작 시 비밀 키를 안전하게 초기화하여,</br>
이후 모든 JWT 관련 작업에서 일관된 키를 사용할 수 있도록 보장</br>
</br>
개발 참고사항 :</br>
기본적인 엔티티와 같은 부분에대한 구현이나 CRUD와 같은 기능 구현에 앞서</br>
JwtTokenProvider.java를 통한 JWT 토큰의 생성/검증</br>
PasswordEncoder.java를 통한 비밀번호 암호화 및 암호화된 비밀번호의 일치여부 검증</br>
과 같은 기능을 우선적으로 구현하여 후에 추가 구현하게될 기능들을</br>
추가적으로 고도화하거나 변경하는 과정에서의 혼선을 사전에 방지하여</br>
보다 안정적인 개발 환경을 확보


