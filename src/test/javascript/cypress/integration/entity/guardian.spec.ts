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

describe('Guardian e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/guardians*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('guardian');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load Guardians', () => {
    cy.intercept('GET', '/api/guardians*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('guardian');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('Guardian').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details Guardian page', () => {
    cy.intercept('GET', '/api/guardians*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('guardian');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('guardian');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create Guardian page', () => {
    cy.intercept('GET', '/api/guardians*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('guardian');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Guardian');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit Guardian page', () => {
    cy.intercept('GET', '/api/guardians*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('guardian');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('Guardian');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should create an instance of Guardian', () => {
    cy.intercept('GET', '/api/guardians*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('guardian');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Guardian');

    cy.get(`[data-cy="fullName"]`).type('technologies', { force: true }).invoke('val').should('match', new RegExp('technologies'));

    cy.get(`[data-cy="idNumber"]`)
      .type('RAM Israeli Plastic', { force: true })
      .invoke('val')
      .should('match', new RegExp('RAM Israeli Plastic'));

    cy.get(`[data-cy="relationshipToApplicant"]`)
      .type('invoice Optimization', { force: true })
      .invoke('val')
      .should('match', new RegExp('invoice Optimization'));

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/guardians*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('guardian');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });

  it('should delete last instance of Guardian', () => {
    cy.intercept('GET', '/api/guardians*').as('entitiesRequest');
    cy.intercept('DELETE', '/api/guardians/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('guardian');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('guardian').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/guardians*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('guardian');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
});
