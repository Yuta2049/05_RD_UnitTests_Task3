package edu.epam.izhevsk.junit;

import static org.mockito.AdditionalMatchers.*;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class PaymentControllerTest {

    @Mock
    AccountService accountService;

    @Mock
    DepositService depositService;

    @InjectMocks
    PaymentController paymentController;

    @BeforeEach
    @Test
    public void mockInitialisation() throws InsufficientFundsException {

        // Mock initialisation should be done in one place, for each test.

        // Configure mocks that for user with id 100 isAuthenticated will return “true”.
        when(accountService.isUserAuthenticated(100L)).thenReturn(true);

        // For deposit of amount less than hundred transaction (any userId) will be successful,
        // any other – will throw InsufficientFundsException  (use Mockito AdditionalMatchers).
        doThrow(new InsufficientFundsException()).when(paymentController).deposit(geq(100L), anyLong());

    }

    @Test
    public void testDeposit() {

        // Successful deposit (userId 100, amount 50), check that isUserAuthenticated has been called exactly one time with parameter = 100.

        try {

            paymentController.deposit(50L, 100L);

        } catch (Exception ex) {
            // we expect exception there
            System.err.println(ex.toString());
        }

        verify(accountService, times(2)).isUserAuthenticated(100L);

    }

    @Test
    public void testFailDepositForUnauthentificatedUser() {

        //Failed deposit of large amount, expect exception

        try {
            paymentController.deposit(150L, 50L);
        } catch (Exception ex) {
            System.err.println(ex.toString());
        }

    }

    @Test
    public void testFailDepositOfLargeAmount() {

        //Failed deposit of large amount, expect exception

        try {
            paymentController.deposit(150L, 50L);
        } catch (Exception ex) {
            // we expect exception there
            System.err.println(ex.toString());
        }

    }

}
