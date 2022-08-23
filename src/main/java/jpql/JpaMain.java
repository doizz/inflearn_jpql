package jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {

    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            member.setTeam(team);

            em.persist(member);

            em.flush();
            em.clear();

            String query = "select m from Member m where m.age IS NOT NULL";

            List<Member> result = em.createQuery(query, Member.class)
                    .setFirstResult(1)
                    .setMaxResults(10)
                    .getResultList();



            tx.commit();
        } catch(Exception e){
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();

    }
}


/**
 * - JPQL 문법
 *  - select m from Member as m where m.age>18
 *  - 엔티티와 속성은 대소 문자 구분 O
 *  - JPQL키워드는 대소문자 구분 X
 *  - 엔티티 이름 사용, 테이블 이름이 아님
 *  - 별칭은 필수
 *
 * - 결과조회 API
 *  - query.getResultList() : 결과가 하나 이상일 때, 리스트 반환 ( 결과가 없으면 빈 리스트 반환)
 *  - query.getSingleResult(): 결과가 정확히 하나, 단일 객체 반환
 *   - 결과가 없으면 : javax.persistence.NoResultException
 *   - 결과가 둘 이상이면 : javax.persistence.NonUniqueResultException
 *
 *
 * - 프로젝션
 *  - select 절에 조회할대상을지정 하는 것
 *  - 프로덕션대상: 엔티티,임베디드 타입, 스칼라 타입
 *  - select m from Member m -> 엔티티 프로젝션
 *  - select m.team from Member m -> 엔티티 프로젝션
 *  - select m.address from Member m -> 임베디드 타입 프로젝션
 *  - select m.username , m.age fromm Member m -> 스칼라 타입프로젝션
 *  - distinct로 중복 제거
 *
 * - 프로젝션 - 여러 값 조회
 *  - select m.username, m.age from Member m
 *
 *  - 1. Query 타입으로 조회
 *  - 2. Object[] 타입으로 조회
 *  - 3. new 명령어로 조회
 *   - 단순값을 DTO로 바로 조회
 *   - select new MemberDTO from Member m
 *   - 패키지 명을 포함한 전체 클래스 명 입력
 *   - 순서와 타입이 일치하는 생성자 필요
 *
 *  - 페이징 API
 *   - JPA는 페이징을 다음 두 API로 추상화
 *   - setFirstResult(int startPosition) : 조회 시작위치 (0부터 시작)
 *   - setMaxResults(int maxResult) 조회할 데이터 수
 * - 조인
 *  - 내부 조인 : select m from Member m [INNER] JOIN m.team t
 *
 *  - 외부 조인 : select m from Member m LEFT [OUTER] JOIN m.team t
 *
 *  - 세타 조인 : select count(m) from Member m, Team t where m.username = t.name
 * - 조인 - ON절
 *  - ON절을 활용한 조인 (JPA 2.1부터 지원)
 *  - 1. 조인대상 필터링
 *  - 2. 연관관계 없는 엔티티 외부 조인 (하이버네이트 5.1부터)
 *
 *  - 조인대상 필터링
 *   - 예) 회원과 팀을 조인하면서, 팀 이름이 A인 팀만 조인
 *   - JPQL : select m , t from Member m left join m.team t on t.name = 'A'
 *   - SQL : select m.*, t.* from Member m left join Team t On m.team_id = t.id and t.name = 'A'
 *
 *  - 연관관계 없는 엔티티 외부 조인
 *   - 예) 회원의 이름과 팀의 이름이 같은 대상 외부 조인
 *   - JPQL : select m, t from Member m left join Team t on.m.username = t.name
 *   - SQL : select m.* , t.* from Member m left join Team t ON  m.username = t.name
 *
 *  - 서브 쿼리
 *   - 나이가 평균 보다 많은 회원
 *    - select m from Member m where m.age > (select avg(m2.age) from Member m2)
 *
 *  - 서브쿼리 지원 함수
 *   - [NOT] EXISTS (subquery) : 서브쿼리에 결과가 존재 하면 참
 *    - (ALL | ANY | SOME) (subquery)
 *    - ALL 모두 만족하면 참
 *    - ANY , SOME : 같은 의미, 조건을 하나라도 만족하면 참
 *
 *   - [NOT] IN (subquery) : 서브쿼리의 결과중 하나라도 같은 것이 있으면 참
 *
 *  - JPA서브 쿼리 한게
 *   - JPA는 where, having 절에서만 서브쿼리 사용 가능
 *   - select 절도 가능 (하이버네이트에서 지원)
 *   - FROM 절의 서브쿼리는 현제 JPQL에서 불가능
 *    - 조인으로 풀 수 있으면 풀어서 해결
 *
 *  - JPQL 타입표현
 *   - 문자: 'HELLO','She"s'
 *   - 숫자 : 10L(Long), 10D(Double), 10F(Float)
 *   - Boolean: TRUE , FALSE
 *   - ENUM : jpabook.Member.Admin(패키지명포함)
 *   - 엔티티 타입 : TYPE(m) = Member(상속관계에서 사용)
 *
 *  - JPQL 기타
 *   - SQL과 문법이 같은 식
 *   - EXISTS, IN
 *   - AND , OR , NOT
 *   - =,>,>= , < , <= <>
 *   - BETWWEN, LIKE, IS NULL
 *
 */