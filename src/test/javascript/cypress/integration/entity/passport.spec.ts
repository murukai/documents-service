import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Passport e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/passports*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('passport');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load Passports', () => {
    cy.intercept('GET', '/api/passports*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('passport');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('Passport').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details Passport page', () => {
    cy.intercept('GET', '/api/passports*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('passport');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('passport');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create Passport page', () => {
    cy.intercept('GET', '/api/passports*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('passport');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Passport');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit Passport page', () => {
    cy.intercept('GET', '/api/passports*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('passport');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('Passport');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should create an instance of Passport', () => {
    cy.intercept('GET', '/api/passports*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('passport');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Passport');

    cy.get(`[data-cy="passportNumber"]`)
      .type('Associate Germany', { force: true })
      .invoke('val')
      .should('match', new RegExp('Associate Germany'));

    cy.get(`[data-cy="issuedAt"]`).type('Vista', { force: true }).invoke('val').should('match', new RegExp('Vista'));

    cy.get(`[data-cy="issuedDate"]`).type('2021-04-03T17:37').invoke('val').should('equal', '2021-04-03T17:37');

    cy.get(`[data-cy="lossDescription"]`)
      .type('deposit cross-platform complexity', { force: true })
      .invoke('val')
      .should('match', new RegExp('deposit cross-platform complexity'));

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/passports*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('passport');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });

  it('should delete last instance of Passport', () => {
    cy.intercept('GET', '/api/passports*').as('entitiesRequest');
    cy.intercept('DELETE', '/api/passports/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('passport');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('passport').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/passports*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('passport');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
});
