package edu.epam.izhevsk.junit;

import static org.mockito.AdditionalMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;


//@RunWith(MockitoJUnitRunner.class)
public class PaymentControllerTest {

    @Mock
    AccountService accountService;

    @Mock
    DepositService depositService;

    @InjectMocks
    PaymentController paymentController;

    @Test
    @BeforeEach
    public void init() throws InsufficientFundsException {

        // Mock initialisation should be done in one place, for each test.
        initMocks(this);

        // Configure mocks that for user with id 100 isAuthenticated will return “true”.
        when(accountService.isUserAuthenticated(100L)).thenReturn(true);

        // For deposit of amount less than hundred transaction (any userId) will be successful,
        // any other – will throw InsufficientFundsException  (use Mockito AdditionalMatchers).
        //doThrow(new InsufficientFundsException()).when(paymentController).deposit(geq(100L), anyLong());
        when(depositService.deposit(geq(100L), anyLong())).thenThrow(InsufficientFundsException.class);

    }

    @Test
    public void testDeposit() throws InsufficientFundsException {

        // Successful deposit (userId 100, amount 50),
        // check that isUserAuthenticated has been called exactly one time with parameter = 100.
        paymentController.deposit(50L, 100L);
        verify(accountService, times(1)).isUserAuthenticated(100L);

    }

    @Test
    public void testFailDepositForUnauthentificatedUser() {

        //Failed deposit for unauthenticated user
        assertThrows(SecurityException.class, () -> paymentController.deposit(150L, 50L));

    }

    @Test
    public void testFailDepositOfLargeAmount() {

        //Failed deposit of large amount, expect exception
        assertThrows(InsufficientFundsException.class, () -> paymentController.deposit(150L, 100L));

    }

}
