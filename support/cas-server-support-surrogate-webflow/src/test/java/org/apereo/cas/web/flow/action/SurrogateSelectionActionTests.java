package org.apereo.cas.web.flow.action;

import org.apereo.cas.authentication.CoreAuthenticationTestUtils;
import org.apereo.cas.authentication.Credential;
import org.apereo.cas.web.support.WebUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.Action;
import org.springframework.webflow.test.MockRequestContext;

import static org.junit.Assert.*;

/**
 * This is {@link SurrogateSelectionActionTests}.
 *
 * @author Misagh Moayyed
 * @since 5.3.0
 */
public class SurrogateSelectionActionTests extends BaseSurrogateInitialAuthenticationActionTests {

    @Autowired
    @Qualifier("selectSurrogateAction")
    private Action selectSurrogateAction;

    @Test
    public void verifyNoCredentialFound() {
        try {
            final MockRequestContext context = new MockRequestContext();
            final MockHttpServletRequest request = new MockHttpServletRequest();
            request.addParameter(SurrogateSelectionAction.PARAMETER_NAME_SURROGATE_TARGET, "cassurrogate");
            context.setExternalContext(new ServletExternalContext(new MockServletContext(), request, new MockHttpServletResponse()));
            assertEquals("success", selectSurrogateAction.execute(context).getId());
            final Credential c = WebUtils.getCredential(context);
            assertNull(c);
        } catch (final Exception e) {
            throw new AssertionError(e);
        }
    }

    @Test
    public void verifyCredentialFound() {
        try {
            final MockRequestContext context = new MockRequestContext();
            WebUtils.putCredential(context, CoreAuthenticationTestUtils.getCredentialsWithSameUsernameAndPassword("casuser"));
            final MockHttpServletRequest request = new MockHttpServletRequest();
            request.addParameter(SurrogateSelectionAction.PARAMETER_NAME_SURROGATE_TARGET, "cassurrogate");
            context.setExternalContext(new ServletExternalContext(new MockServletContext(), request, new MockHttpServletResponse()));
            assertEquals("success", selectSurrogateAction.execute(context).getId());
            final Credential c = WebUtils.getCredential(context);
            assertTrue(c.getId().contains(request.getParameter(SurrogateSelectionAction.PARAMETER_NAME_SURROGATE_TARGET)));
        } catch (final Exception e) {
            throw new AssertionError(e);
        }
    }
}
