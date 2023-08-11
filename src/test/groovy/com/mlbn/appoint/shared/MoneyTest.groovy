package com.mlbn.appoint.shared

import spock.lang.Specification
import com.mlbn.appoint.shared.vo.Money

class MoneyTest extends Specification {

     def 'money must be positive'() {
        expect:
        Money.of(new BigDecimal(-1)).isLeft()
    }

    def 'can create valid money'() {
        expect:
        Money.of(BigDecimal.ZERO).isRight()
        Money.of(BigDecimal.TEN).isRight()
    }
}
