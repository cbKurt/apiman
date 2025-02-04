import io.apiman.manager.api.beans.events.ApimanEventHeaders;
import io.apiman.manager.api.beans.events.ContractApprovalEvent;
import io.apiman.manager.api.beans.events.ContractCreatedEvent;
import io.apiman.manager.api.beans.events.IVersionedApimanEvent;
import io.apiman.manager.api.beans.idm.UserDto;
import io.apiman.manager.api.beans.notifications.NotificationCategory;
import io.apiman.manager.api.beans.notifications.NotificationStatus;
import io.apiman.manager.api.beans.notifications.dto.NotificationDto;
import io.apiman.manager.api.core.config.ApiManagerConfig;
import io.apiman.manager.api.notifications.email.EmailNotificationDispatcher;
import io.apiman.manager.api.notifications.email.QuteTemplateEngine;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 *
 *
 * @author Marc Savy {@literal <marc@blackparrotlabs.io>}
 */

public class DefaultTemplatesTest {

    ApiManagerConfig CONFIG = new ApiManagerConfig() {

        @Override
        public Path getConfigDirectory() {
            return Paths.get("src/main/resources");
        }

        @Override
        public String getApimanManagerUiEndpoint() {
            return StringUtils.removeEnd("http://localhost:8080/apimanui/api-manager/", "/");
        }
    };

    @Test
    public void contract_approval_request_html() throws IOException {
        var appDeveloper = new UserDto()
                .setFullName("John Smith Appdev")
                .setUsername("JohnSmith123")
                .setEmail("foo@apiman.io")
                .setLocale(Locale.ENGLISH);

        ContractCreatedEvent contractCreated = ContractCreatedEvent
                .builder()
                .setHeaders(ApimanEventHeaders.builder()
                        .setId("Event123")
                        .setSource(URI.create("https://example.org"))
                        .setSubject("Hello")
                        .setEventVersion(1L)
                        .setTime(OffsetDateTime.now())
                        .setType("X")
                        .build())
                .setApiOrgId("ApiOrg")
                .setApiId("CoolApi")
                .setApiVersion("1.0")
                .setClientOrgId("MobileKompany")
                .setClientId("MobileApp")
                .setClientVersion("2.0")
                .setContractId("1234")
                .setPlanId("Gold")
                .setPlanVersion("1.3")
                .setApprovalRequired(true)
                .setUser(appDeveloper)
                .build();

        UserDto recipient = new UserDto()
                .setEmail("approver@apiman.io")
                .setUsername("ApproverPerson")
                .setFullName("David Approver")
                .setLocale(Locale.ENGLISH);

        NotificationDto<IVersionedApimanEvent> notificationDto = new NotificationDto<>()
                .setId(123L)
                .setCategory(NotificationCategory.API_ADMINISTRATION)
                .setReason("whatever")
                .setReasonMessage("hi")
                .setStatus(NotificationStatus.OPEN)
                .setCreatedOn(OffsetDateTime.now())
                .setModifiedOn(OffsetDateTime.now())
                .setRecipient(recipient)
                .setSource("adsadsaad")
                .setPayload(contractCreated);

        QuteTemplateEngine engine = new QuteTemplateEngine(CONFIG);
        engine.applyTemplate(
                Files.readString(CONFIG.getConfigDirectory().resolve("notifications/email/tpl/en/apiman.client.contract.approval.request.html")),
                EmailNotificationDispatcher.createDefaultTemplateMap(notificationDto, CONFIG)
        );
    }

    @Test
    public void contract_approval_request_txt() throws IOException {
        var appDeveloper = new UserDto()
                .setFullName("John Smith Appdev")
                .setUsername("JohnSmith123")
                .setEmail("foo@apiman.io")
                .setLocale(Locale.ENGLISH);

        ContractCreatedEvent contractCreated = ContractCreatedEvent
                .builder()
                .setHeaders(ApimanEventHeaders.builder()
                        .setId("Event123")
                        .setSource(URI.create("https://example.org"))
                        .setSubject("Hello")
                        .setEventVersion(1L)
                        .setTime(OffsetDateTime.now())
                        .setType("X")
                        .build())
                .setApiOrgId("ApiOrg")
                .setApiId("CoolApi")
                .setApiVersion("1.0")
                .setClientOrgId("MobileKompany")
                .setClientId("MobileApp")
                .setClientVersion("2.0")
                .setContractId("1234")
                .setPlanId("Gold")
                .setPlanVersion("1.3")
                .setApprovalRequired(true)
                .setUser(appDeveloper)
                .build();

        UserDto recipient = new UserDto()
                .setEmail("approver@apiman.io")
                .setUsername("ApproverPerson")
                .setFullName("David Approver")
                .setLocale(Locale.ENGLISH);

        NotificationDto<IVersionedApimanEvent> notificationDto = new NotificationDto<>()
                .setId(123L)
                .setCategory(NotificationCategory.API_ADMINISTRATION)
                .setReason("whatever")
                .setReasonMessage("hi")
                .setStatus(NotificationStatus.OPEN)
                .setCreatedOn(OffsetDateTime.now())
                .setModifiedOn(OffsetDateTime.now())
                .setRecipient(recipient)
                .setSource("adsadsaad")
                .setPayload(contractCreated);

        QuteTemplateEngine engine = new QuteTemplateEngine(CONFIG);
        engine.applyTemplate(
                Files.readString(CONFIG.getConfigDirectory().resolve("notifications/email/tpl/en/apiman.client.contract.approval.request.txt")),
                EmailNotificationDispatcher.createDefaultTemplateMap(notificationDto, CONFIG)
        );
    }

    @Test
    public void contract_approval_response_html() throws IOException {
        var appDeveloper = new UserDto()
                .setFullName("John Smith Appdev")
                .setUsername("JohnSmith123")
                .setEmail("foo@apiman.io")
                .setLocale(Locale.ENGLISH);

        ContractApprovalEvent approvalEvent = ContractApprovalEvent
                .builder()
                .setHeaders(ApimanEventHeaders.builder()
                        .setId("Event123")
                        .setSource(URI.create("https://example.org"))
                        .setSubject("Hello")
                        .setEventVersion(1L)
                        .setTime(OffsetDateTime.now())
                        .setType("X")
                        .build())
                .setApiOrgId("ApiOrg")
                .setApiId("CoolApi")
                .setApiVersion("1.0")
                .setClientOrgId("MobileKompany")
                .setClientId("MobileApp")
                .setClientVersion("2.0")
                .setContractId("1234")
                .setPlanId("Gold")
                .setPlanVersion("1.3")
                .setApprover(appDeveloper)
                .setApproved(true)
                .build();

        UserDto recipient = new UserDto()
                .setEmail("approver@apiman.io")
                .setUsername("ApproverPerson")
                .setFullName("David Approver")
                .setLocale(Locale.ENGLISH);

        NotificationDto<IVersionedApimanEvent> notificationDto = new NotificationDto<>()
                .setId(123L)
                .setCategory(NotificationCategory.API_ADMINISTRATION)
                .setReason("whatever")
                .setReasonMessage("hi")
                .setStatus(NotificationStatus.OPEN)
                .setCreatedOn(OffsetDateTime.now())
                .setModifiedOn(OffsetDateTime.now())
                .setRecipient(recipient)
                .setSource("adsadsaad")
                .setPayload(approvalEvent);

        QuteTemplateEngine engine = new QuteTemplateEngine(CONFIG);
        engine.applyTemplate(
                Files.readString(CONFIG.getConfigDirectory().resolve("notifications/email/tpl/en/apiman.client.contract.approval.granted.html")),
                EmailNotificationDispatcher.createDefaultTemplateMap(notificationDto, CONFIG)
        );
    }

    @Test
    public void contract_approval_response_txt() throws IOException {
        var appDeveloper = new UserDto()
                .setFullName("John Smith Appdev")
                .setUsername("JohnSmith123")
                .setEmail("foo@apiman.io")
                .setLocale(Locale.ENGLISH);

        ContractApprovalEvent approvalEvent = ContractApprovalEvent
                .builder()
                .setHeaders(ApimanEventHeaders.builder()
                        .setId("Event123")
                        .setSource(URI.create("https://example.org"))
                        .setSubject("Hello")
                        .setEventVersion(1L)
                        .setTime(OffsetDateTime.now())
                        .setType("X")
                        .build())
                .setApiOrgId("ApiOrg")
                .setApiId("CoolApi")
                .setApiVersion("1.0")
                .setClientOrgId("MobileKompany")
                .setClientId("MobileApp")
                .setClientVersion("2.0")
                .setContractId("1234")
                .setPlanId("Gold")
                .setPlanVersion("1.3")
                .setApprover(appDeveloper)
                .setApproved(true)
                .build();

        UserDto recipient = new UserDto()
                .setEmail("approver@apiman.io")
                .setUsername("ApproverPerson")
                .setFullName("David Approver")
                .setLocale(Locale.ENGLISH);

        NotificationDto<IVersionedApimanEvent> notificationDto = new NotificationDto<>()
                .setId(123L)
                .setCategory(NotificationCategory.API_ADMINISTRATION)
                .setReason("whatever")
                .setReasonMessage("hi")
                .setStatus(NotificationStatus.OPEN)
                .setCreatedOn(OffsetDateTime.now())
                .setModifiedOn(OffsetDateTime.now())
                .setRecipient(recipient)
                .setSource("adsadsaad")
                .setPayload(approvalEvent);

        QuteTemplateEngine engine = new QuteTemplateEngine(CONFIG);
        engine.applyTemplate(
                Files.readString(CONFIG.getConfigDirectory().resolve("notifications/email/tpl/en/apiman.client.contract.approval.granted.txt")),
                EmailNotificationDispatcher.createDefaultTemplateMap(notificationDto, CONFIG)
        );
    }
}
