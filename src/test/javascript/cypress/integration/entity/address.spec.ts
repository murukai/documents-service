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

describe('Address e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/addresses*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('address');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load Addresses', () => {
    cy.intercept('GET', '/api/addresses*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('address');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('Address').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details Address page', () => {
    cy.intercept('GET', '/api/addresses*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('address');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('address');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create Address page', () => {
    cy.intercept('GET', '/api/addresses*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('address');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Address');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit Address page', () => {
    cy.intercept('GET', '/api/addresses*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('address');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('Address');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  /* this test is commented because it contains required relationships
  it('should create an instance of Address', () => {
    cy.intercept('GET', '/api/addresses*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('address');
    cy.wait('@entitiesRequest')
      .then(({ request, response }) => startingEntitiesCount = response.body.length);
    cy.get(entityCreateButtonSelector).click({force: true});
    cy.getEntityCreateUpdateHeading('Address');

    cy.get(`[data-cy="streetAddress"]`).type('Rubber line applications', { force: true }).invoke('val').should('match', new RegExp('Rubber line applications'));


    cy.get(`[data-cy="postalCode"]`).type('Sports withdrawal Architect', { force: true }).invoke('val').should('match', new RegExp('Sports withdrawal Architect'));


    cy.get(`[data-cy="city"]`).type('Thompsonside', { force: true }).invoke('val').should('match', new RegExp('Thompsonside'));


    cy.get(`[data-cy="stateProvince"]`).type('supply-chains Pants', { force: true }).invoke('val').should('match', new RegExp('supply-chains Pants'));


    cy.get(`[data-cy="placeOfBirth"]`).type('Berkshire Buckinghamshire', { force: true }).invoke('val').should('match', new RegExp('Berkshire Buckinghamshire'));


    cy.get(`[data-cy="telephone"]`).type('(619) 721-0671', { force: true }).invoke('val').should('match', new RegExp('(619) 721-0671'));

    cy.setFieldSelectToLastOfEntity('country');

    cy.setFieldSelectToLastOfEntity('applicant');

    cy.get(entityCreateSaveButtonSelector).click({force: true});
    cy.scrollTo('top', {ensureScrollable: false});
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/addresses*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('address');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });
  */

  /* this test is commented because it contains required relationships
  it('should delete last instance of Address', () => {
    cy.intercept('GET', '/api/addresses*').as('entitiesRequest');
    cy.intercept('DELETE', '/api/addresses/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('address');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({force: true});
        cy.getEntityDeleteDialogHeading('address').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({force: true});
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/addresses*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('address');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
  */
});
