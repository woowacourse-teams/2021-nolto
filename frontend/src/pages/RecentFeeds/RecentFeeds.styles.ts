import styled from 'styled-components';

import HighLightedText from 'components/@common/HighlightedText/HighlightedText';

const Root = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 4rem 0;
`;

const SectionTitle = styled(HighLightedText)`
  margin-bottom: 48px;
`;

const RecentToysContainer = styled.div`
  height: 45rem;
`;

export default {
  Root,
  SectionTitle,
  RecentToysContainer,
};
