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

describe('DemographicDetails e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/demographic-details*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('demographic-details');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load DemographicDetails', () => {
    cy.intercept('GET', '/api/demographic-details*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('demographic-details');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('DemographicDetails').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details DemographicDetails page', () => {
    cy.intercept('GET', '/api/demographic-details*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('demographic-details');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('demographicDetails');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create DemographicDetails page', () => {
    cy.intercept('GET', '/api/demographic-details*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('demographic-details');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('DemographicDetails');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit DemographicDetails page', () => {
    cy.intercept('GET', '/api/demographic-details*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('demographic-details');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('DemographicDetails');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should create an instance of DemographicDetails', () => {
    cy.intercept('GET', '/api/demographic-details*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('demographic-details');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('DemographicDetails');

    cy.get(`[data-cy="eyeColor"]`).select('BLACK');

    cy.get(`[data-cy="hairColor"]`).select('BLACK');

    cy.get(`[data-cy="visibleMarks"]`)
      .type('National Future', { force: true })
      .invoke('val')
      .should('match', new RegExp('National Future'));

    cy.get(`[data-cy="profession"]`)
      .type('synthesize Rial facilitate', { force: true })
      .invoke('val')
      .should('match', new RegExp('synthesize Rial facilitate'));

    cy.get(`[data-cy="placeOfBirth"]`).type('hack', { force: true }).invoke('val').should('match', new RegExp('hack'));

    cy.setFieldImageAsBytesOfEntity('image', 'integration-test.png', 'image/png');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/demographic-details*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('demographic-details');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });

  it('should delete last instance of DemographicDetails', () => {
    cy.intercept('GET', '/api/demographic-details*').as('entitiesRequest');
    cy.intercept('DELETE', '/api/demographic-details/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('demographic-details');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('demographicDetails').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/demographic-details*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('demographic-details');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
});
