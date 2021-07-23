import styled from 'styled-components';

import TextButton from 'components/@common/TextButton/TextButton';

const Root = styled.div`
  margin: 0.25rem 0;
  width: 100%;
  display: flex;
  flex-wrap: wrap;
`;

export const TechButton = styled(TextButton.Rounded)`
  width: fit-content;
  height: 1.5rem;
  padding: 0 0.5rem;
  margin: 0.125rem 0.125rem;
`;

export default { Root };
